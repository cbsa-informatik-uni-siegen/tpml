/**
 * TODO
 */
package de.unisiegen.tpml.core.typeinference;

import de.unisiegen.tpml.core.typechecker.DefaultTypeSubstitution;
import de.unisiegen.tpml.core.typechecker.TypeEquation;
import de.unisiegen.tpml.core.typechecker.TypeEquationList;

/**
 * TODO
 *
 * @author Benjamin Mies
 *
 */
public class TypeSubstitutionList {

	//
	//Constants
	//
	
		public static final TypeSubstitutionList EMPTY_LIST = new TypeSubstitutionList();
	

		  //
		  // Attributes
		  //

		  /**
		   * The first TypeSubstitution in the list.
		   */
		  private DefaultTypeSubstitution first;
		  
		  /**
		   * The remaining equations or <code>null</code>.
		   */
		  private TypeSubstitutionList remaining;
		  
		  //
		  // Constructors (private)
		  //
		  
		  private TypeSubstitutionList() {
			    super();
			  }
	
		  
		  private TypeSubstitutionList(DefaultTypeSubstitution pFirst, TypeSubstitutionList pRemaining) {
			    if (pFirst == null) {
			      throw new NullPointerException("first is null");
			    }
			    if (pRemaining == null) {
			      throw new NullPointerException("remaining is null");
			    }
			    this.first = pFirst;
			    this.remaining = pRemaining;
			  }
		  
		  //
		  // Base Methods
		  //
		  
		  @Override
		  public String toString() {
		    StringBuilder builder = new StringBuilder(128);
		    builder.append('{');
		    for (TypeSubstitutionList list = this; list != EMPTY_LIST; list = list.remaining) {
		      if (list != this)
		        builder.append(", ");
		      builder.append(list.first.toString());
		    }
		    builder.append('}');
		    return builder.toString();
		  }


		public TypeSubstitutionList extend(DefaultTypeSubstitution s) {
			return new TypeSubstitutionList( s, this);
			
		}


		public DefaultTypeSubstitution getFirst() {
			return this.first;
		}
}
