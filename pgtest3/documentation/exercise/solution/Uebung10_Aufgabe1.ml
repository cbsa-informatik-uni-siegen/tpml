(* Uebungsblatt 10 / Aufgabe 1 *)

type ttype =
    TINT | TBOOL | TUNIT |
    TLIST of ttype
    | TPAIR of ttype * ttype (* der Einfachheit halber *)
    | TARROW of ttype * ttype
    | TOBJECT of (string * ttype) list;;

exception Incompatible_types;;

let rec supremum tau1 tau2 = (* tau1 v tau2 *)
  if tau1 = tau2 then
    tau1
  else match (tau1, tau2) with
      ((TLIST (t1)), (TLIST (t2))) -> TLIST (supremum t1 t2)
    | ((TPAIR (t1a, t1b)), (TPAIR (t2a, t2b))) ->
	TPAIR (supremum t1a t2a, supremum t1b t2b)
    | ((TARROW (t1a, t1b)), (TARROW (t2a, t2b))) ->
	(* Obacht, Parametertyp bei tau1 -> tau2 *)
	TARROW (infimum t1a t2a, supremum t1b t2b)
    | ((TOBJECT (row1)), (TOBJECT (row2))) ->
	TOBJECT (supremum_row row1 row2)
    | _ -> raise Incompatible_types
and supremum_row row1 row2 =
  if row1 = [] then
    []
  else
    let (m1, t1) = List.hd row1 in
      try
	(* Typ fuer m1 in row2 suchen *)
	let t2 = List.assoc m1 row2 in
	  (* Supremum t1 t2 fuer m1 *)
	  (m1, supremum t1 t2) ::
	    (* Supremum der Restreihe *)
	    (supremum_row
		(List.tl row1)
		(List.remove_assoc m1 row2))
      with Not_found ->
	(* m1 nicht in row2 *)
	supremum_row (List.tl row1) row2
and infimum tau1 tau2 = (* tau1 ^ tau2 *)
  if tau1 = tau2 then
    tau1
  else match (tau1, tau2) with
      ((TLIST (t1)), (TLIST (t2))) -> TLIST (infimum t1 t2)
    | ((TPAIR (t1a, t1b)), (TPAIR (t2a, t2b))) ->
	TPAIR (infimum t1a t2a, infimum t1b t2b)
    | ((TARROW (t1a, t1b)), (TARROW (t2a, t2b))) ->
	TARROW (supremum t1a t2a, infimum t1b t2b)
    | ((TOBJECT (row1)), (TOBJECT (row2))) ->
	TOBJECT (infimum_row row1 row2)
    | _ -> raise Incompatible_types
and infimum_row row1 row2 =
  if row1 = [] && row2 = [] then
    [] (* Beide Reihen abgearbeitet *)
  else if row1 = [] then
    (* row2 noch abarbeiten *)
    infimum_row row2 row1
  else
    (* erste Methode aus row1 *)
    let (m1, t1) = List.hd row1 in
      try
	(* gucken, ob auch in row2 *)
	let t2 = List.assoc m1 row2 in
	  (* infimum t1 ^ t2 *)
	  (m1, infimum t1 t2) ::
	    (* plus infimum Restreihe *)
	    (infimum_row
		(List.tl row1)
		(List.remove_assoc m1 row2))
      with Not_found ->
	(* m1 nur in row1 *)
	(m1, t1) :: (infimum_row (List.tl row1) row2);;

let t1 = TOBJECT ([("a", TINT); ("b", TBOOL)]);;
let t2 = TOBJECT ([("a", TINT); ("u", TUNIT)]);;
supremum t1 t2;;
infimum t1 t2;;
