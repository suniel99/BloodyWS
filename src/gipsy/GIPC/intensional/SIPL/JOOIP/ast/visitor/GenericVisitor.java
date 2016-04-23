/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.CompilationUnit;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.ImportDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.Node;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.PackageDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.TypeParameter;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.AnnotationDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.AnnotationMemberDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.ClassOrInterfaceDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.ConstructorDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EmptyMemberDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EmptyTypeDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EnumConstantDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EnumDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.FieldDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.InitializerDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.MethodDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.Parameter;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.VariableDeclarator;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.VariableDeclaratorId;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ArrayAccessExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ArrayCreationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ArrayInitializerExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.AssignExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.BinaryExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.BooleanLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.CastExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.CharLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ClassExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ConditionalExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.DoubleLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.EnclosedExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.FieldAccessExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.InstanceOfExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.IntegerLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.IntegerLiteralMinValueExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.LongLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.LongLiteralMinValueExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.MarkerAnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.MemberValuePair;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.MethodCallExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.NameExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.NormalAnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.NullLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ObjectCreationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.QualifiedNameExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.SingleMemberAnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.StringLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.SuperExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.SuperMemberAccessExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ThisExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.UnaryExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.VariableDeclarationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.AssertStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.BlockStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.BreakStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.CatchClause;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ContinueStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.DoStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.EmptyStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ExplicitConstructorInvocationStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ExpressionStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ForStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ForeachStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.IfStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.LabeledStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ReturnStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.SwitchEntryStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.SwitchStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.SynchronizedStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ThrowStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.TryStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.TypeDeclarationStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.WhileStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.ClassOrInterfaceType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.PrimitiveType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.ReferenceType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.VoidType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.WildcardType;

/**
 * @author Julio Vilmar Gesser
 */
public interface GenericVisitor<R, A> {

    public R visit(Node n, A arg);

    //- Compilation Unit ----------------------------------

    public R visit(CompilationUnit n, A arg);

    public R visit(PackageDeclaration n, A arg);

    public R visit(ImportDeclaration n, A arg);

    public R visit(TypeParameter n, A arg);

    //- Body ----------------------------------------------

    public R visit(ClassOrInterfaceDeclaration n, A arg);

    public R visit(EnumDeclaration n, A arg);

    public R visit(EmptyTypeDeclaration n, A arg);

    public R visit(EnumConstantDeclaration n, A arg);

    public R visit(AnnotationDeclaration n, A arg);

    public R visit(AnnotationMemberDeclaration n, A arg);

    public R visit(FieldDeclaration n, A arg);

    public R visit(VariableDeclarator n, A arg);

    public R visit(VariableDeclaratorId n, A arg);

    public R visit(ConstructorDeclaration n, A arg);

    public R visit(MethodDeclaration n, A arg);

    public R visit(Parameter n, A arg);

    public R visit(EmptyMemberDeclaration n, A arg);

    public R visit(InitializerDeclaration n, A arg);

    //- Type ----------------------------------------------

    public R visit(ClassOrInterfaceType n, A arg);

    public R visit(PrimitiveType n, A arg);

    public R visit(ReferenceType n, A arg);

    public R visit(VoidType n, A arg);

    public R visit(WildcardType n, A arg);

    //- Expression ----------------------------------------

    public R visit(ArrayAccessExpr n, A arg);

    public R visit(ArrayCreationExpr n, A arg);

    public R visit(ArrayInitializerExpr n, A arg);

    public R visit(AssignExpr n, A arg);

    public R visit(BinaryExpr n, A arg);

    public R visit(CastExpr n, A arg);

    public R visit(ClassExpr n, A arg);

    public R visit(ConditionalExpr n, A arg);

    public R visit(EnclosedExpr n, A arg);

    public R visit(FieldAccessExpr n, A arg);

    public R visit(InstanceOfExpr n, A arg);

    public R visit(StringLiteralExpr n, A arg);

    public R visit(IntegerLiteralExpr n, A arg);

    public R visit(LongLiteralExpr n, A arg);

    public R visit(IntegerLiteralMinValueExpr n, A arg);

    public R visit(LongLiteralMinValueExpr n, A arg);

    public R visit(CharLiteralExpr n, A arg);

    public R visit(DoubleLiteralExpr n, A arg);

    public R visit(BooleanLiteralExpr n, A arg);

    public R visit(NullLiteralExpr n, A arg);

    public R visit(MethodCallExpr n, A arg);

    public R visit(NameExpr n, A arg);

    public R visit(ObjectCreationExpr n, A arg);

    public R visit(QualifiedNameExpr n, A arg);

    public R visit(SuperMemberAccessExpr n, A arg);

    public R visit(ThisExpr n, A arg);

    public R visit(SuperExpr n, A arg);

    public R visit(UnaryExpr n, A arg);

    public R visit(VariableDeclarationExpr n, A arg);

    public R visit(MarkerAnnotationExpr n, A arg);

    public R visit(SingleMemberAnnotationExpr n, A arg);

    public R visit(NormalAnnotationExpr n, A arg);

    public R visit(MemberValuePair n, A arg);

    //- Statements ----------------------------------------

    public R visit(ExplicitConstructorInvocationStmt n, A arg);

    public R visit(TypeDeclarationStmt n, A arg);

    public R visit(AssertStmt n, A arg);

    public R visit(BlockStmt n, A arg);

    public R visit(LabeledStmt n, A arg);

    public R visit(EmptyStmt n, A arg);

    public R visit(ExpressionStmt n, A arg);

    public R visit(SwitchStmt n, A arg);

    public R visit(SwitchEntryStmt n, A arg);

    public R visit(BreakStmt n, A arg);

    public R visit(ReturnStmt n, A arg);

    public R visit(IfStmt n, A arg);

    public R visit(WhileStmt n, A arg);

    public R visit(ContinueStmt n, A arg);

    public R visit(DoStmt n, A arg);

    public R visit(ForeachStmt n, A arg);

    public R visit(ForStmt n, A arg);

    public R visit(ThrowStmt n, A arg);

    public R visit(SynchronizedStmt n, A arg);

    public R visit(TryStmt n, A arg);

    public R visit(CatchClause n, A arg);

}
