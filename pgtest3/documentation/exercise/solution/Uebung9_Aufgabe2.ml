(* Uebung 9 / Aufgabe 2 *)

type ttype =
    TINT | TBOOL | TUNIT |
    TLIST of ttype
    | TPAIR of ttype * ttype (* der Einfachheit halber *)
    | TARROW of ttype * ttype
    | TOBJECT of (string * ttype) list;;

let rec subtype_row row1 row2 = (* (S-OBJECT) *)
  if row2 = [] then
    true (* jedes Objekt ist Subtyp von <> *)
  else
    (* erste Methode aus row2... *)
    let (m2, t2) = List.hd row2 in
      try
	(* ...in row1 suchen... *)
	let t1 = List.assoc m2 row1 in
	  (* t1 <: t2 und row1' <: row2' *)
	  (subtype t1 t2)
	  && (subtype_row (List.remove_assoc m2 row1) (List.tl row2))
      with
	  Not_found -> false
and subtype tau1 tau2 =
  if tau1 = tau2 then (* (S-REFL) *)
    true
  else
    match (tau1, tau2) with
      | ((TLIST tau1'), (TLIST tau2')) -> (* (S-LIST) *)
	  subtype tau1' tau2'
      | ((TPAIR (tau1a, tau1b)), (TPAIR (tau2a, tau2b))) -> (* (S-PRODUCT *)
	  (subtype tau1a tau2a) && (subtype tau1b tau2b)
      | ((TARROW (tau1a, tau1b)), (TARROW (tau2a, tau2b))) -> (* (S-ARROW) *)
	  (subtype tau2a tau1a) && (subtype tau1b tau2b)
      | ((TOBJECT row1), (TOBJECT row2)) -> (* (S-OBJECT) *)
	  subtype_row row1 row2
      | _ -> false;;

(* Einfaches Beispiel: <a:int, b:bool> <: <b: bool> *)
let tau1 = TOBJECT ([("a", TINT); ("b", TBOOL)]) in
let tau2 = TOBJECT ([("b", TBOOL)]) in
  subtype tau1 tau2;;
