(* Uebung 12 / Aufgabe 3 *)

type ttype =
    TINT | TBOOL | TUNIT
    | TVAR of string
    | TREC of string * ttype
    | TARROW of ttype * ttype
    | TOBJECT of (string * ttype) list;;

(* Annahmen *)
type tasm = (ttype * ttype) list;;

let rec subst (t: ttype) (id: string) (nt: ttype) =
  match t with
      TINT | TBOOL | TUNIT -> t
    | TVAR (id') -> if id = id' then nt else t
    | TREC (id', t') ->
	if id = id' then t
	else TREC (id', subst t' id nt)
    | TARROW (t1, t2) ->
	TARROW (subst t1 id nt, subst t2 id nt)
    | TOBJECT (row) ->
	TOBJECT (List.map (fun (m, t) -> (m, subst t id nt)) row);;

(* rek. Typen auffalten *)
let unfold (id: string) (t: ttype) =
  subst t id (TREC (id, t));;

let rec subtype_row asm row1 row2 =
  if row2 = [] then
    true (* jeder Objekttyp ist Subtyp von <> *)
  else
    (* erste Methode aus row2... *)
    let (m2, t2) = List.hd row2 in
      try
	(* ...in row1 suchen... *)
	let t1 = List.assoc m2 row1 in
	  (* t1 <: t2 und row1' <: row2' *)
	  (subtype asm t1 t2)
	  && (subtype_row asm (List.remove_assoc m2 row1) (List.tl row2))
      with
	  Not_found -> false
and subtype asm tau1 tau2 =
  if tau1 = tau2 then (* (S-REFL) *)
    true
  else if List.mem (tau1, tau2) asm then (* Subtyp nach Annahme *)
    true
  else
    let asm' = (tau1, tau2) :: asm in
      match (tau1, tau2) with
	| ((TREC (n, t)), _) -> (* links auffalten *)
	    (subtype asm' (unfold n t) tau2)
	| (_, (TREC (n, t))) -> (* rechts auffalten *)
	    (subtype asm' tau1 (unfold n t))
	| ((TARROW (tau1a, tau1b)), (TARROW (tau2a, tau2b))) -> (* (S-ARROW) *)
	    (subtype asm' tau2a tau1a) && (subtype asm' tau1b tau2b)
	| ((TOBJECT row1), (TOBJECT row2)) -> (* (S-OBJECT) *)
	    subtype_row asm' row1 row2
	| _ -> false;;

(* Einfaches Beispiel: <a:int, b:bool> <: <b: bool> *)
let tau1 = TOBJECT ([("a", TINT); ("b", TBOOL)]) in
let tau2 = TOBJECT ([("b", TBOOL)]) in
  subtype [] tau1 tau2;;
