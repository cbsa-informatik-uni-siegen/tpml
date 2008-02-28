package de.unisiegen.tpml.core.expressions;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.unisiegen.tpml.core.interfaces.DefaultExpressions;
import de.unisiegen.tpml.core.interfaces.DefaultIdentifiers;
import de.unisiegen.tpml.core.interfaces.DefaultTypes;
import de.unisiegen.tpml.core.interfaces.ShowBondsInput;
import de.unisiegen.tpml.core.latex.DefaultLatexCommand;
import de.unisiegen.tpml.core.latex.DefaultLatexInstruction;
import de.unisiegen.tpml.core.latex.LatexCommandList;
import de.unisiegen.tpml.core.latex.LatexInstructionList;
import de.unisiegen.tpml.core.latex.LatexPackage;
import de.unisiegen.tpml.core.latex.LatexPackageList;
import de.unisiegen.tpml.core.latex.LatexPrintable;
import de.unisiegen.tpml.core.latex.LatexString;
import de.unisiegen.tpml.core.latex.LatexStringBuilder;
import de.unisiegen.tpml.core.latex.LatexStringBuilderFactory;
import de.unisiegen.tpml.core.prettyprinter.PrettyPrintable;
import de.unisiegen.tpml.core.prettyprinter.PrettyString;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilder;
import de.unisiegen.tpml.core.prettyprinter.PrettyStringBuilderFactory;
import de.unisiegen.tpml.core.typechecker.TypeSubstitution;
import de.unisiegen.tpml.core.types.MonoType;
import de.unisiegen.tpml.core.types.TypeName;
import de.unisiegen.tpml.core.util.Theme;


/**
 * Base class for all classes in the expression hierarchy.
 * 
 * @author Benedikt Meurer
 * @author Christian Fehler
 * @version $Rev:1053 $
 * @see PrettyPrintable
 */
public abstract class Expression implements Cloneable, PrettyPrintable,
    PrettyPrintPriorities, LatexPrintable, ShowBondsInput
{

  /**
   * A level-order enumeration of the expressions within a given expression.
   * Used to implement the {@link Expression#levelOrderEnumeration()} method.
   * 
   * @see Expression#levelOrderEnumeration()
   */
  private class LevelOrderEnumeration implements Enumeration < Expression >
  {

    /**
     * The queue.
     */
    private LinkedList < Expression > queue = new LinkedList < Expression > ();


    /**
     * Initializes a new <code>LevelOrderEnumeration</code>.
     * 
     * @param pExpression The input <code>Expression</code>.
     */
    LevelOrderEnumeration ( Expression pExpression )
    {
      this.queue.add ( pExpression );
    }


    /**
     * Returns true, if there are more elements, otherwise false.
     * 
     * @return True, if there are more elements, otherwise false.
     * @see Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements ()
    {
      return !this.queue.isEmpty ();
    }


    /**
     * Returns the next element.
     * 
     * @return The next element.
     * @see Enumeration#nextElement()
     */
    public Expression nextElement ()
    {
      Expression e = this.queue.poll ();
      this.queue.addAll ( e.children () );
      return e;
    }
  }


  /**
   * The resource bundle.
   */
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle ( "de.unisiegen.tpml.core.expressions.messages" ); //$NON-NLS-1$


  /**
   * Prefix of the {@link Expression}.
   */
  protected static final String PREFIX_EXPRESSION = "e"; //$NON-NLS-1$


  /**
   * Prefix of the {@link Expression} which is a value.
   */
  protected static final String PREFIX_VALUE = "v"; //$NON-NLS-1$


  /**
   * Prefix of the {@link Expression} which is a constant.
   */
  protected static final String PREFIX_CONST = "c"; //$NON-NLS-1$


  /**
   * Prefix of the {@link Inherit}.
   */
  protected static final String PREFIX_BODY = "b"; //$NON-NLS-1$


  /**
   * Prefix of the {@link Row}.
   */
  protected static final String PREFIX_ROW = "r"; //$NON-NLS-1$


  /**
   * Prefix of the {@link Row} which is a value.
   */
  protected static final String PREFIX_ROW_VALUE = "\u03C9"; //$NON-NLS-1$


  /**
   * Prefix of {@link Identifier}s after the first.
   */
  public static final String PREFIX_ID_V = "id"; //$NON-NLS-1$


  /**
   * Prefix of the first {@link Identifier} of {@link Method}s.
   */
  public static final String PREFIX_ID_M = "m"; //$NON-NLS-1$


  /**
   * Prefix of the first {@link Identifier} of {@link Attribute}s.
   */
  public static final String PREFIX_ID_A = "a"; //$NON-NLS-1$


  /**
   * Prefix of the first {@link Identifier} of {@link ObjectExpr}s.
   */
  public static final String PREFIX_ID_S = "self"; //$NON-NLS-1$


  /**
   * Prefix of the {@link Exn}.
   */
  protected static final String PREFIX_EXN = "ep"; //$NON-NLS-1$


  /**
   * Prefix of the {@link BinaryOperator}.
   */
  protected static final String PREFIX_BINARYOPERATOR = "op"; //$NON-NLS-1$


  /**
   * The unused string.
   */
  private static final String UNUSED = "unused"; //$NON-NLS-1$


  /**
   * Gets the caption for the given class from the resource bundle.
   * 
   * @param pClass The given class.
   * @return The caption of the given class.
   */
  public static String getCaption ( java.lang.Class < ? > pClass )
  {
    try
    {
      return RESOURCE_BUNDLE.getString ( pClass.getSimpleName () );
    }
    catch ( MissingResourceException e )
    {
      return pClass.getSimpleName ();
    }
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public static LatexCommandList getLatexCommandsStatic ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( new DefaultLatexCommand ( LATEX_PARENTHESIS, 1, "(#1)", //$NON-NLS-1$
        "e" ) ); //$NON-NLS-1$
    return commands;
  }


  /**
   * Returns a set of needed latex instructions for this latex printable object.
   * 
   * @return A set of needed latex instructions for this latex printable object.
   */
  public static LatexInstructionList getLatexInstructionsStatic ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    Color colorExpression = Theme.currentTheme ().getExpressionColor ();
    float red = ( float ) Math
        .round ( ( ( float ) colorExpression.getRed () ) / 255 * 100 ) / 100;
    float green = ( float ) Math.round ( ( ( float ) colorExpression
        .getGreen () ) / 255 * 100 ) / 100;
    float blue = ( float ) Math
        .round ( ( ( float ) colorExpression.getBlue () ) / 255 * 100 ) / 100;
    instructions
        .add ( new DefaultLatexInstruction (
            "\\definecolor{" + LATEX_COLOR_EXPRESSION + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
                + red + "," //$NON-NLS-1$
                + green + "," //$NON-NLS-1$
                + blue + "}", LATEX_COLOR_EXPRESSION + ": color of expression text" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    Color colorKeyword = Theme.currentTheme ().getKeywordColor ();
    red = ( float ) Math
        .round ( ( ( float ) colorKeyword.getRed () ) / 255 * 100 ) / 100;
    green = ( float ) Math
        .round ( ( ( float ) colorKeyword.getGreen () ) / 255 * 100 ) / 100;
    blue = ( float ) Math
        .round ( ( ( float ) colorKeyword.getBlue () ) / 255 * 100 ) / 100;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_KEYWORD + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}", LATEX_COLOR_KEYWORD + ": color of keywords" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    Color colorConstant = Theme.currentTheme ().getConstantColor ();
    red = ( float ) Math
        .round ( ( ( float ) colorConstant.getRed () ) / 255 * 100 ) / 100;
    green = ( float ) Math
        .round ( ( ( float ) colorConstant.getGreen () ) / 255 * 100 ) / 100;
    blue = ( float ) Math
        .round ( ( ( float ) colorConstant.getBlue () ) / 255 * 100 ) / 100;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_CONSTANT + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}", LATEX_COLOR_CONSTANT + ": color of constants" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    Color colorIdentifier = Theme.currentTheme ().getIdentifierColor ();
    red = ( float ) Math
        .round ( ( ( float ) colorIdentifier.getRed () ) / 255 * 100 ) / 100;
    green = ( float ) Math
        .round ( ( ( float ) colorIdentifier.getGreen () ) / 255 * 100 ) / 100;
    blue = ( float ) Math
        .round ( ( ( float ) colorIdentifier.getBlue () ) / 255 * 100 ) / 100;
    instructions.add ( new DefaultLatexInstruction (
        "\\definecolor{" + LATEX_COLOR_IDENTIFIER + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
            + red + "," //$NON-NLS-1$
            + green + "," //$NON-NLS-1$
            + blue + "}", LATEX_COLOR_IDENTIFIER + ": color of identifiers" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    Color colorBindingId = Theme.currentTheme ().getBindingIdColor ();
    red = ( float ) Math
        .round ( ( ( float ) colorBindingId.getRed () ) / 255 * 100 ) / 100;
    green = ( float ) Math
        .round ( ( ( float ) colorBindingId.getGreen () ) / 255 * 100 ) / 100;
    blue = ( float ) Math
        .round ( ( ( float ) colorBindingId.getBlue () ) / 255 * 100 ) / 100;
    instructions
        .add ( new DefaultLatexInstruction (
            "\\definecolor{" + LATEX_COLOR_BINDING_ID + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
                + red + "," //$NON-NLS-1$
                + green + "," //$NON-NLS-1$
                + blue + "}", LATEX_COLOR_BINDING_ID + ": color of binding identifiers" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    Color colorBoundId = Theme.currentTheme ().getBoundIdColor ();
    red = ( float ) Math
        .round ( ( ( float ) colorBoundId.getRed () ) / 255 * 100 ) / 100;
    green = ( float ) Math
        .round ( ( ( float ) colorBoundId.getGreen () ) / 255 * 100 ) / 100;
    blue = ( float ) Math
        .round ( ( ( float ) colorBoundId.getBlue () ) / 255 * 100 ) / 100;
    instructions
        .add ( new DefaultLatexInstruction (
            "\\definecolor{" + LATEX_COLOR_BOUND_ID + "}{rgb}{" //$NON-NLS-1$ //$NON-NLS-2$
                + red + "," //$NON-NLS-1$
                + green + "," //$NON-NLS-1$
                + blue + "}", LATEX_COLOR_BOUND_ID + ": color of bound identifiers" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public static LatexPackageList getLatexPackagesStatic ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( LatexPackage.COLOR );
    return packages;
  }


  /**
   * Cached <code>TreeSet</code> of the free Identifiers, so the free
   * Identifier do not need to be determined on every invocation of
   * {@link #getIdentifiersFree()}.
   * 
   * @see #getIdentifiersFree()
   */
  protected ArrayList < Identifier > identifiersFree = null;


  /**
   * A list of all {@link Identifier}s in this {@link Expression}.
   * 
   * @see #getIdentifiersAll()
   */
  private ArrayList < Identifier > identifiersAll = null;


  /**
   * Cached {@link PrettyStringBuilder}, so the {@link PrettyStringBuilder} do
   * not need to be determined on every invocation of
   * {@link #toPrettyStringBuilder(PrettyStringBuilderFactory)}.
   * 
   * @see #toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  protected PrettyStringBuilder prettyStringBuilder = null;


  /**
   * A list of lists of bound {@link Identifier}s in this {@link Expression}.
   */
  protected ArrayList < ArrayList < Identifier >> boundIdentifiers = null;


  /**
   * Cached liyt of sub expressions, so the children do not need to be
   * determined on every invocation of {@link #children()}.
   * 
   * @see #children()
   */
  private ArrayList < Expression > children = null;


  /**
   * Cached vector of {@link Attribute} {@link Identifier}.
   * 
   * @see #getDomA()
   */
  protected ArrayList < Identifier > domA = null;


  /**
   * The parent of this {@link Expression}.
   * 
   * @see #getParent()
   * @see #setParent(PrettyPrintable)
   */
  protected PrettyPrintable parent = null;


  /**
   * The start offset of this {@link Expression} in the source code.
   * 
   * @see #getParserStartOffset()
   * @see #setParserStartOffset(int)
   */
  protected int parserStartOffset = -1;


  /**
   * The end offset of this {@link Expression} in the source code.
   * 
   * @see #getParserEndOffset()
   * @see #setParserEndOffset(int)
   */
  protected int parserEndOffset = -1;


  /**
   * The prefix of this {@link Expression}.
   * 
   * @see #getPrefix()
   */
  protected String prefix = null;


  /**
   * The list of the free {@link TypeName}s in this {@link Expression}.
   */
  private ArrayList < TypeName > typeNamesFree = null;


  /**
   * Returns an {@link ArrayList} of the child {@link Expression}s.
   * 
   * @return An {@link ArrayList} of the child {@link Expression}s.
   */
  public final ArrayList < Expression > children ()
  {
    if ( this.children == null )
    {
      if ( this instanceof DefaultExpressions )
      {
        Expression [] expressions = ( ( DefaultExpressions ) this )
            .getExpressions ();
        this.children = new ArrayList < Expression > ( expressions.length );
        for ( Expression expression : expressions )
        {
          this.children.add ( expression );
        }
      }
      else
      {
        this.children = new ArrayList < Expression > ( 0 );
      }
    }
    return this.children;
  }


  /**
   * Clones this expression, so that the result is an expression equal to this
   * expression, but with a different object identity. This is used in the
   * substitution of expressions to be able to distinguish different appearances
   * of the same identifier in the pretty printer, as required by the
   * highlighting of bound variables.
   * 
   * @return a deep clone of this object.
   * @see Object#clone()
   */
  @Override
  public abstract Expression clone ();


  /**
   * Returns <code>true</code> if the expression contains any of the memory
   * operations, that is either {@link Assign}, {@link Deref} or {@link Ref}.
   * Otherwise <code>false</code> will be returned. This method is used for
   * the {@link de.unisiegen.tpml.core.interpreters.InterpreterProofModel}s to
   * enable the
   * {@link de.unisiegen.tpml.core.interpreters.InterpreterProofModel#isMemoryEnabled()}
   * property depending on whether the expression at the root node contains
   * memory operations or not. This method uses the
   * {@link #levelOrderEnumeration()} method to traverse all sub expressions, so
   * this method does not need to be implemented by any derived class, but it'll
   * work automagically.
   * 
   * @return <code>true</code> if the expression contains any of the memory
   *         operations, <code>false</code> otherwise.
   * @see #children()
   * @see #levelOrderEnumeration()
   */
  public final boolean containsMemoryOperations ()
  {
    Enumeration < Expression > enumeration = levelOrderEnumeration ();
    while ( enumeration.hasMoreElements () )
    {
      Expression e = enumeration.nextElement ();
      if ( ( e instanceof Assign ) || ( e instanceof Deref )
          || ( e instanceof Ref ) )
      {
        return true;
      }
    }
    return false;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#equals(Object)
   */
  @Override
  public abstract boolean equals ( Object pObject );


  /**
   * Returns the caption of this {@link Expression}.
   * 
   * @return The caption of this {@link Expression}.
   */
  public abstract String getCaption ();


  /**
   * Returns a list of all {@link Attribute} {@link Identifier}s in the domain
   * of this {@link Expression}.
   * 
   * @return A list of all {@link Attribute} {@link Identifier}s in the domain
   *         of this {@link Expression}.
   */
  public ArrayList < Identifier > getDomA ()
  {
    if ( this.domA == null )
    {
      this.domA = new ArrayList < Identifier > ();
      for ( Expression child : children () )
      {
        this.domA.addAll ( child.getDomA () );
      }
    }
    return this.domA;
  }


  /**
   * Returns true, if a free Identifier is not a variable and not a method
   * {@link Identifier}, otherwise false.
   * 
   * @return True, if a free Identifier is not a variable and not a method
   *         {@link Identifier}, otherwise false.
   */
  public final boolean getIdentifierFreeNotOnlyVariable ()
  {
    for ( Identifier id : getIdentifiersFree () )
    {
      if ( ! ( ( Identifier.Set.VARIABLE.equals ( id.getSet () ) ) || ( Identifier.Set.METHOD
          .equals ( id.getSet () ) ) ) )
      {
        return true;
      }
    }
    return false;
  }


  /**
   * Returns a list of all {@link Identifier}s in this {@link Expression}. For
   * example "lambda id.x" will return a list with the members "id" and "x".
   * This method should only be used to check the disjunction of the
   * {@link Identifier}s.
   * 
   * @return A list of all {@link Identifier}s in this {@link Expression}.
   * @see #identifiersAll
   */
  public final ArrayList < Identifier > getIdentifiersAll ()
  {
    if ( this.identifiersAll == null )
    {
      this.identifiersAll = new ArrayList < Identifier > ();
      if ( this instanceof Identifier )
      {
        this.identifiersAll.add ( ( Identifier ) this );
        return this.identifiersAll;
      }
      if ( this instanceof DefaultIdentifiers )
      {
        Identifier [] identifiers = ( ( DefaultIdentifiers ) this )
            .getIdentifiers ();
        for ( Identifier id : identifiers )
        {
          this.identifiersAll.add ( id );
        }
      }
      for ( Expression child : children () )
      {
        this.identifiersAll.addAll ( child.getIdentifiersAll () );
      }
    }
    return this.identifiersAll;
  }


  /**
   * Returns the free (unbound) identifiers within the expression, e.g. the name
   * of the identifier for an identifier expression or the free identifiers for
   * its sub expressions in applications, abstractions and recursions. The
   * default implementation in the {@link Expression} class uses introspection
   * to determine the sub expressions and calls <code>free()</code>
   * recursively on all sub expressions. Some of the derived classes might need
   * to override this method if they represents a binding mechanism, like
   * {@link Let}, or if they don't have sub expressions, but provide free
   * identifiers, like {@link Identifier}.
   * 
   * @return the set of free (unbound) identifiers within the expression.
   */
  public ArrayList < Identifier > getIdentifiersFree ()
  {
    if ( this.identifiersFree == null )
    {
      this.identifiersFree = new ArrayList < Identifier > ();
      for ( Expression child : children () )
      {
        this.identifiersFree.addAll ( child.getIdentifiersFree () );
      }
    }
    return this.identifiersFree;
  }


  /**
   * Returns a set of needed latex commands for this latex printable object.
   * 
   * @return A set of needed latex commands for this latex printable object.
   */
  public LatexCommandList getLatexCommands ()
  {
    LatexCommandList commands = new LatexCommandList ();
    commands.add ( getLatexCommandsStatic () );
    if ( this instanceof DefaultExpressions )
    {
      commands.add ( ( ( DefaultExpressions ) this ).getExpressions () );
    }
    if ( this instanceof DefaultIdentifiers )
    {
      commands.add ( ( ( DefaultIdentifiers ) this ).getIdentifiers () );
    }
    if ( this instanceof DefaultTypes )
    {
      commands.add ( ( ( DefaultTypes ) this ).getTypes () );
    }
    return commands;
  }


  /**
   * Returns a list of needed latex instructions for this latex printable
   * object.
   * 
   * @return A lsit of needed latex instructions for this latex printable
   *         object.
   */
  public LatexInstructionList getLatexInstructions ()
  {
    LatexInstructionList instructions = new LatexInstructionList ();
    instructions.add ( getLatexInstructionsStatic () );
    if ( this instanceof DefaultExpressions )
    {
      instructions.add ( ( ( DefaultExpressions ) this ).getExpressions () );
    }
    if ( this instanceof DefaultIdentifiers )
    {
      instructions.add ( ( ( DefaultIdentifiers ) this ).getIdentifiers () );
    }
    if ( this instanceof DefaultTypes )
    {
      instructions.add ( ( ( DefaultTypes ) this ).getTypes () );
    }
    return instructions;
  }


  /**
   * Returns a set of needed latex packages for this latex printable object.
   * 
   * @return A set of needed latex packages for this latex printable object.
   */
  public LatexPackageList getLatexPackages ()
  {
    LatexPackageList packages = new LatexPackageList ();
    packages.add ( getLatexPackagesStatic () );
    if ( this instanceof DefaultExpressions )
    {
      packages.add ( ( ( DefaultExpressions ) this ).getExpressions () );
    }
    if ( this instanceof DefaultIdentifiers )
    {
      packages.add ( ( ( DefaultIdentifiers ) this ).getIdentifiers () );
    }
    if ( this instanceof DefaultTypes )
    {
      packages.add ( ( ( DefaultTypes ) this ).getTypes () );
    }
    return packages;
  }


  /**
   * Returns the parent of this {@link Expression}.
   * 
   * @return The parent of this {@link Expression}.
   * @see #parent
   * @see #setParent(PrettyPrintable)
   */
  public final PrettyPrintable getParent ()
  {
    return this.parent;
  }


  /**
   * Returns the parserEndOffset.
   * 
   * @return The parserEndOffset.
   * @see #parserEndOffset
   * @see #setParserEndOffset(int)
   */
  public int getParserEndOffset ()
  {
    return this.parserEndOffset;
  }


  /**
   * Returns the parserStartOffset.
   * 
   * @return The parserStartOffset.
   * @see #parserStartOffset
   * @see #setParserStartOffset(int)
   */
  public int getParserStartOffset ()
  {
    return this.parserStartOffset;
  }


  /**
   * Returns the prefix of this {@link Expression}.
   * 
   * @return The prefix of this {@link Expression}.
   * @see #prefix
   */
  public String getPrefix ()
  {
    if ( this.prefix == null )
    {
      if ( this.isValue () )
      {
        this.prefix = PREFIX_VALUE;
      }
      else
      {
        this.prefix = PREFIX_EXPRESSION;
      }
    }
    return this.prefix;
  }


  /**
   * Returns a list of the free {@link TypeName}s in this {@link Expression}.
   * 
   * @return A list of the free {@link TypeName}s in this {@link Expression}.
   */
  public ArrayList < TypeName > getTypeNamesFree ()
  {
    if ( this.typeNamesFree == null )
    {
      this.typeNamesFree = new ArrayList < TypeName > ();
      MonoType [] types = null;
      if ( this instanceof DefaultTypes )
      {
        types = ( ( DefaultTypes ) this ).getTypes ();
      }
      if ( types != null )
      {
        for ( MonoType type : types )
        {
          if ( type != null )
          {
            this.typeNamesFree.addAll ( type.getTypeNamesFree () );
          }
        }
      }
      for ( Expression child : children () )
      {
        this.typeNamesFree.addAll ( child.getTypeNamesFree () );
      }
    }
    return this.typeNamesFree;
  }


  /**
   * {@inheritDoc}
   * 
   * @see Object#hashCode()
   */
  @Override
  public abstract int hashCode ();


  /**
   * Returns <code>true</code> if the expression should be considered an
   * exception in the big and small step interpreters and must thereby not be
   * evaluated any futher, nor must any operation be performed on exceptions.
   * The default implementation in the <code>Expression</code> class simply
   * returns <code>false</code>, so derived classes will need to override
   * this method if their instances should be considered values under certain
   * circumstances.
   * 
   * @return <code>true</code> if the expression is an exception,
   *         <code>false</code> otherwise.
   */
  public boolean isException ()
  {
    return false;
  }


  /**
   * Returns <code>true</code> if the expression should be considered a value
   * in the big and small step interpreters and must thereby not be evaluated
   * any further. The default implementation in the <code>Expression</code>
   * class simply returns <code>false</code>, so derived classes will need to
   * override this method if their instances should be considered values under
   * certain circumstances.
   * 
   * @return <code>true</code> if the expression is a value,
   *         <code>false</code> otherwise.
   */
  public boolean isValue ()
  {
    return false;
  }


  /**
   * Returns an {@link Enumeration} that enumerates the expression within the
   * expression hierarchy starting at this expression in level order (that is
   * breadth first enumeration).
   * 
   * @return a breadth first enumeration of all expressions within the
   *         expression hierarchy starting at this item.
   */
  public Enumeration < Expression > levelOrderEnumeration ()
  {
    return new LevelOrderEnumeration ( this );
  }


  /**
   * Sets the parent of this {@link Expression}.
   * 
   * @param pParent The parent to set.
   * @see #parent
   * @see #getParent()
   */
  public final void setParent ( PrettyPrintable pParent )
  {
    this.parent = pParent;
  }


  /**
   * Sets the parser end offset.
   * 
   * @param pParserEndOffset The new parser end offset.
   * @see #getParserEndOffset()
   * @see #parserEndOffset
   */
  public void setParserEndOffset ( int pParserEndOffset )
  {
    this.parserEndOffset = pParserEndOffset;
  }


  /**
   * Sets the parser start offset.
   * 
   * @param pParserStartOffset The new parser start offset.
   * @see #getParserStartOffset()
   * @see #parserStartOffset
   */
  public void setParserStartOffset ( int pParserStartOffset )
  {
    this.parserStartOffset = pParserStartOffset;
  }


  /**
   * Substitutes the expression <code>e</code> for the identifier
   * <code>id</code> in this expression, and returns the resulting expression.
   * The resulting expression may be a new <code>Expression</code> object or
   * if no substitution took place, the same object. The method operates
   * recursively.
   * 
   * @param pId the name of the identifier.
   * @param pExpression the <code>Expression</code> to substitute.
   * @return the resulting expression.
   * @throws NullPointerException if <code>id</code> or </code>e</code> is
   *           <code>null</code>.
   */
  public abstract Expression substitute ( Identifier pId, Expression pExpression );


  /**
   * Applies the type <code>substitution</code> to this expression, to be
   * exact, to the types within this expression, and returns the new expression
   * with the new types. If the expression does not contain any types, this
   * method simply returns a reference to this expression.
   * 
   * @param pTypeSubstitution the type substitution to apply.
   * @return the resulting expression.
   * @throws NullPointerException if <code>substitution</code> is
   *           <code>null</code>.
   */
  public Expression substitute ( @SuppressWarnings ( UNUSED )
  TypeSubstitution pTypeSubstitution )
  {
    return this;
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexString()
   */
  public final LatexString toLatexString ()
  {
    return toLatexStringBuilder ( LatexStringBuilderFactory.newInstance (), 0 )
        .toLatexString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see LatexPrintable#toLatexStringBuilder(LatexStringBuilderFactory,int)
   */
  public abstract LatexStringBuilder toLatexStringBuilder (
      LatexStringBuilderFactory pLatexStringBuilderFactory, int pIndent );


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyString()
   */
  public final PrettyString toPrettyString ()
  {
    return toPrettyStringBuilder ( PrettyStringBuilderFactory.newInstance () )
        .toPrettyString ();
  }


  /**
   * {@inheritDoc}
   * 
   * @see PrettyPrintable#toPrettyStringBuilder(PrettyStringBuilderFactory)
   */
  public abstract PrettyStringBuilder toPrettyStringBuilder (
      PrettyStringBuilderFactory pPrettyStringBuilderFactory );


  /**
   * Returns the string representation for this expression. This method is
   * mainly used for debugging.
   * 
   * @return The pretty printed string representation for this expression.
   * @see #toPrettyString()
   * @see Object#toString()
   */
  @Override
  public final String toString ()
  {
    return toPrettyString ().toString ();
  }
}
