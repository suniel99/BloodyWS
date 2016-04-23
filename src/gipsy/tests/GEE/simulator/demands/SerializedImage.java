package gipsy.tests.GEE.simulator.demands;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * XXX.
 * @author Emil Vassev
 * @since
 * @version $Id: SerializedImage.java,v 1.6 2009/09/29 19:10:35 mokhov Exp $
 */
public class SerializedImage
implements Serializable 
{
	/*
	 * Constants
	 */

	/**
	 * XXX: fix. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A BufferedImage to serialized.
	 * @serial oBufferentImage A BufferedImage to serialized.
	 */
	private BufferedImage oBufferentImage;

	/**
	 * Create a SerializedImage with a BufferedImage (<I>can be used with RMI</I>).
	 * @param poBufferedImage Initial BufferedImage.
	 */
	public SerializedImage(BufferedImage poBufferedImage) 
	{
		this.oBufferentImage=poBufferedImage;
	}

	/**
	 * Create a SerializedImage (<I>can be used with RMI</I>).
	 * @param poImage Initial Image.
	 * @param piWidth Width of this image.
	 * @param Height height of this image.
	 */
	public SerializedImage(Image poImage, int piWidth, int piHeight)
	{
		boolean bErrorFlag = false;

		PixelGrabber oGrabber = new PixelGrabber(poImage, 0, 0, piWidth, piHeight, true);
		
		try
		{
			oGrabber.grabPixels();
		}
		catch(InterruptedException e)
		{
			System.out.println("* Error while creating SerializedImage from Image");
			bErrorFlag = true;
		}
		
		this.oBufferentImage = new BufferedImage(piWidth,piHeight,BufferedImage.TYPE_INT_RGB);
		
		if(!bErrorFlag)
		{
			this.oBufferentImage.setRGB(0, 0, piWidth, piHeight, (int[])oGrabber.getPixels(), 0, piWidth);
		}
	}

	/**
	 * Get an BufferedImage of this SerializedImage.
	 * @return XXX
	 */
	public BufferedImage getBufferedImage()
	{
		return this.oBufferentImage;
	}

	/**
	 * Get the width of this SerializedImage.
	 * @return Width in pixels.
	 */
	public int getWidth()
	{
		return this.oBufferentImage.getWidth();
	}

	/**
	 * Get the height of this SerializedImage.
	 * @return Height in pixels.
	 */
	public int getHeight()
	{
		return this.oBufferentImage.getHeight();
	}

	/**
	 * Returns a string representation of the object.
	 * Like this : SerializedImage[w=bimageWidth,h=bimageHeight].
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new String("SerializedImage[w=" + this.oBufferentImage.getWidth() + ",h=" + this.oBufferentImage.getHeight() + "]");
	}

	/**
	 * Serialization of the BufferedImage.
	 * @param poOOS XXX
	 * @throws IOException XXX
	 */
	private void writeObject(ObjectOutputStream poOOS)
	throws IOException
	{
		poOOS.writeInt(this.oBufferentImage.getWidth());
		poOOS.writeInt(this.oBufferentImage.getHeight());
		poOOS.writeInt(this.oBufferentImage.getType());
		
		for(int i = 0; i < this.oBufferentImage.getWidth(); i++)
		{
			for(int j = 0; j < this.oBufferentImage.getHeight(); j++)
			{
				poOOS.writeInt(this.oBufferentImage.getRGB(i, j));
			}
		}
		
		poOOS.flush();
	}

	/**
	 * XXX.
	 * @param poOIS XXX
	 * @throws IOException XXX
	 * @throws ClassNotFoundException XXX
	 */
	private void readObject(ObjectInputStream poOIS)
	throws IOException, ClassNotFoundException
	{
		this.oBufferentImage = new BufferedImage(poOIS.readInt(), poOIS.readInt(), poOIS.readInt());

		for(int i = 0; i < this.oBufferentImage.getWidth(); i++)
		{
			for(int j = 0; j < this.oBufferentImage.getHeight(); j++)
			{
				this.oBufferentImage.setRGB(i, j, poOIS.readInt());
			}
		}
	}
}

// EOF
