(* Uebungsblatt 6 / Aufgabe 5 *)

(* Typvariablen sind durchnummeriert *)
type tvar = int;;

(* Typen *)
type ttype =
    TUNIT
    | TBOOL
    | TINT
    | TVAR of tvar
    | TARROW of ttype * ttype;;

(* Typgleichungen *)
type teqn = ttype * ttype;;
type teqns = teqn list;;

(* Typvariablen *)
open Set;;
module TVarBase = struct 
  type t = tvar;;
  let compare (i: t) (j: t) = if i < j then -1 else if i > j then 1 else 0;;
end;;
module TVarSet = Set.Make(TVarBase);;

(* Substitutionen *)
module Subst = struct
  (* Substitution dargestellt als Liste von Paaren (Typvariablennummer, Typ) *)
  type t = (tvar * ttype) list;;

  (* Die leere Substitution *)
  let empty: t = [];;

  (* Erstellt eine neue Substitution [tau/alpha] *)
  let make (tau: ttype) (alpha: tvar): t = [alpha, tau];;

  (* Liefert den Definitionsbereich von s *)
  let rec domain (s: t): TVarSet.t =
    match s with
	[] -> TVarSet.empty
      | (alpha, tau) :: s' -> TVarSet.union (TVarSet.singleton alpha) (domain s');;

  (* Wendet s auf die Typvariablennummer alpha an *)
  let apply_to_tvar (s: t) (alpha: tvar): ttype =
    try List.assoc alpha s with Not_found -> TVAR alpha;;

  (* Wendet s auf den Typ tau an *)
  let rec apply_to_type (s: t) (tau: ttype): ttype =
    match tau with
	TINT | TUNIT | TBOOL -> tau
      | TVAR alpha -> apply_to_tvar s alpha
      | TARROW (tau1, tau2) -> TARROW (apply_to_type s tau1, apply_to_type s tau2);;

  (* Wendet s auf die Typgleichung tau1 = tau2 an *)
  let apply_to_eqn (s: t) (tau1, tau2: teqn): teqn = (apply_to_type s tau1, apply_to_type s tau2);;

  (* Wendet s auf Typgleichungen an *)
  let apply_to_eqns (s: t): teqns -> teqns = List.map (apply_to_eqn s);;

  (* Liefert die Komposition von s1 und s2 *)
  let compose (s1: t) (s2: t): t =
    let alphas = TVarSet.union (domain s1) (domain s2) in
      List.map (fun alpha -> (alpha, apply_to_type s2 (apply_to_tvar s1 alpha))) (TVarSet.elements alphas);;
end;;

(* Unifikation *)
exception Cannot_Unify of teqn;;

(* Prueft ob alpha in tau vorkommt *)
let rec occurs (alpha: tvar) (tau: ttype) =
  match tau with
      TVAR alpha' -> alpha = alpha'
    | TARROW (tau1, tau2) -> (occurs alpha tau1) || (occurs alpha tau2)
    | _ -> false;;

(* Unifiziert die Gleichungen eqns *)
let rec unify (eqns: teqns): Subst.t =
  match eqns with
      [] -> Subst.empty
    | eqn :: eqns' ->
	if fst eqn = snd eqn then unify eqns'
	else match eqn with
	    (TVAR alpha, tau) | (tau, TVAR alpha) ->
	      if not (occurs alpha tau) then 
		let s1 = Subst.make tau alpha in
		let s2 = unify (Subst.apply_to_eqns s1 eqns') in
		  Subst.compose s1 s2
	      else
		raise (Cannot_Unify eqn)
	  | (TARROW (tau1, tau2), TARROW (tau1', tau2')) ->
	      unify ((tau1, tau1') :: (tau2, tau2') :: eqns')
	  | _ -> raise (Cannot_Unify eqn);;

(* Am Beispiel aus der Vorlesung *)
let eqns = [
    TARROW (TINT, TVAR 1), TARROW (TINT, TARROW (TVAR 2, TINT));
    TARROW (TVAR 2, TVAR 1), TARROW (TVAR 3, TARROW (TINT, TINT))
  ];;
unify eqns;;
