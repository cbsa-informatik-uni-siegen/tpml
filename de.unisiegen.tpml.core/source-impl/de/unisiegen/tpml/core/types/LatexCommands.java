package de.unisiegen.tpml.core.types ;


import de.unisiegen.tpml.core.latex.LatexStringBuilder ;


/**
 * This interface includes the latex print commands.
 * 
 * @author Christian Fehler
 * @see Type
 * @see LatexStringBuilder
 */
public interface LatexCommands
{
  /**
   * The latex print command for a space.
   */
  public static final String LATEX_SPACE = "\\ " ; //$NON-NLS-1$


  /**
   * The latex print command for a dot.
   */
  public static final String LATEX_DOT = "." ; //$NON-NLS-1$


  /**
   * The latex print command for a colon.
   */
  public static final String LATEX_COLON = "\\colon" ; //$NON-NLS-1$


  /**
   * The latex print command for a comma.
   */
  public static final String LATEX_COMMA = "," ; //$NON-NLS-1$


  /**
   * The latex print command for a semi.
   */
  public static final String LATEX_SEMI = ";" ; //$NON-NLS-1$


  /**
   * The latex print command for a mult.
   */
  public static final String LATEX_MULT = "*" ; //$NON-NLS-1$


  /**
   * The latex print command for a bar.
   */
  public static final String LATEX_BAR = "'" ; //$NON-NLS-1$


  /**
   * The latex print command for an empty string.
   */
  public static final String LATEX_EMPTY_STRING = "" ; //$NON-NLS-1$


  /**
   * The latex print command for a for all.
   */
  public static final String LATEX_FORALL = "\\forall" ; //$NON-NLS-1$


  /**
   * The latex print command for an empty set.
   */
  public static final String LATEX_EMPTYSET = "\\emptyset" ; //$NON-NLS-1$


  /**
   * The latex print command for a bold attr.
   */
  public static final String LATEX_BOLD_ATTR = "boldAttr" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ArrowType}.
   */
  public static final String LATEX_ARROW_TYPE = "TypeArrowType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link BooleanType}.
   */
  public static final String LATEX_BOOLEAN_TYPE = "TypeBooleanType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ClassType}.
   */
  public static final String LATEX_CLASS_TYPE = "TypeClassType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link IntegerType}.
   */
  public static final String LATEX_INTEGER_TYPE = "TypeIntegerType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ListType}.
   */
  public static final String LATEX_LIST_TYPE = "TypeListType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link ObjectType}.
   */
  public static final String LATEX_OBJECT_TYPE = "TypeObjectType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link PolyType}.
   */
  public static final String LATEX_POLY_TYPE = "TypePolyType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link RecType}.
   */
  public static final String LATEX_REC_TYPE = "TypeRecType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link RefType}.
   */
  public static final String LATEX_REF_TYPE = "TypeRefType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link RowType}.
   */
  public static final String LATEX_ROW_TYPE = "TypeRowType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TupleType}.
   */
  public static final String LATEX_TUPLE_TYPE = "TypeTupleType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeName}.
   */
  public static final String LATEX_TYPE_NAME = "TypeTypeName" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link TypeVariable}.
   */
  public static final String LATEX_TYPE_VARIABLE = "TypeTypeVariable" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link UnifyType}.
   */
  public static final String LATEX_UNIFY_TYPE = "TypeUnifyType" ; //$NON-NLS-1$


  /**
   * The latex print command for {@link UnitType}.
   */
  public static final String LATEX_UNIT_TYPE = "TypeUnitType" ; //$NON-NLS-1$
}
