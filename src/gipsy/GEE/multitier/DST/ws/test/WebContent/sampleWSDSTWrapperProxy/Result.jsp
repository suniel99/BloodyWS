<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleWSDSTWrapperProxyid" scope="session" class="test.WSDSTWrapperProxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleWSDSTWrapperProxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleWSDSTWrapperProxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
            java.lang.String endpoint_0idTemp = null;
        if(!endpoint_0id.equals("")){
         endpoint_0idTemp  = endpoint_0id;
        }
        sampleWSDSTWrapperProxyid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        test.WSDSTWrapper getWSDSTWrapper10mtemp = sampleWSDSTWrapperProxyid.getWSDSTWrapper();
if(getWSDSTWrapper10mtemp == null){
%>
<%=getWSDSTWrapper10mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
</TABLE>
<%
}
break;
case 21:
        gotMethod = true;
        test.WSDemand getPendingDemand21mtemp = sampleWSDSTWrapperProxyid.getPendingDemand();
if(getPendingDemand21mtemp == null){
%>
<%=getPendingDemand21mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">signature:</TD>
<TD>
<%
if(getPendingDemand21mtemp != null){
test.WSDemandSignature typesignature24 = getPendingDemand21mtemp.getSignature();
        if(typesignature24!= null){
        String tempsignature24 = typesignature24.toString();
        %>
        <%=tempsignature24%>
        <%
        }}%>
</TD>
</TABLE>
<%
}
break;
case 26:
        gotMethod = true;
        %>
        <jsp:useBean id="test1WSDemandSignature_1id" scope="session" class="test.WSDemandSignature" />
        <%
        test.WSDemand getHashTableValue26mtemp = sampleWSDSTWrapperProxyid.getHashTableValue(test1WSDemandSignature_1id);
if(getHashTableValue26mtemp == null){
%>
<%=getHashTableValue26mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">signature:</TD>
<TD>
<%
if(getHashTableValue26mtemp != null){
test.WSDemandSignature typesignature29 = getHashTableValue26mtemp.getSignature();
        if(typesignature29!= null){
        String tempsignature29 = typesignature29.toString();
        %>
        <%=tempsignature29%>
        <%
        }}%>
</TD>
</TABLE>
<%
}
break;
case 33:
        gotMethod = true;
        %>
        <jsp:useBean id="test1WSDemandSignature_3id" scope="session" class="test.WSDemandSignature" />
        <%
        %>
        <jsp:useBean id="test1WSDemand_2id" scope="session" class="test.WSDemand" />
        <%
        test1WSDemand_2id.setSignature(test1WSDemandSignature_3id);
        test.WSDemandSignature setHashTable33mtemp = sampleWSDSTWrapperProxyid.setHashTable(test1WSDemand_2id);
if(setHashTable33mtemp == null){
%>
<%=setHashTable33mtemp %>
<%
}else{
        if(setHashTable33mtemp!= null){
        String tempreturnp34 = setHashTable33mtemp.toString();
        %>
        <%=tempreturnp34%>
        <%
        }}
break;
}
} catch (Exception e) { 
%>
Exception: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.toString()) %>
Message: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.getMessage()) %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>