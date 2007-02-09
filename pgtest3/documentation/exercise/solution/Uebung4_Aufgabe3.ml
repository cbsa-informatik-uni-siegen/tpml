

(* Uebungsblatt 4 / Aufgabe 3 *)

(* Bezeichner sind einfache Strings *)
type identifier = string;;

(* arithmetische/relationale Operatoren *)
type aoperator = Add | Sub | Mul | Div | Mod;;
type roperator = Eq | Less | Greater | Leq | Geq;;

(* Konstanten *)
type constant =
    Unit
    | Int of int
    | Bool of bool
    | Aop of aoperator
    | Rop of roperator;;

(* Ausdruecke und Typen *)
type expression =
    Const of constant
    | Id of identifier
    | App of expression * expression
    | Cond of expression * expression * expression
    | Let of identifier * expression * expression
    | Abstr of identifier * ttype * expression
    | Rec of identifier * ttype * expression
and ttype =
    TUNIT
    | TBOOL
    | TINT
    | TARROW of ttype * ttype;;


(* Typumgebungen *)
exception Unbound_Identifier of identifier;;

module TEnv = struct
  (* der Typ fuer Typumgebungen *)
  type t = (identifier * ttype) list;;

  (* die leere Typumgebung *)
  let empty: t = [];;

  (* erweitert gamma um [tau/id] *)
  let extend (gamma: t) (id: identifier) (tau: ttype): t = (id, tau) :: gamma;;

  (* sucht nach dem Typ fuer den Bezeichner id in gamma *)
  let rec lookup (gamma: t) (id: identifier): ttype =
    match gamma with
	[] -> raise (Unbound_Identifier id)
      | (id', tau') :: gamma' -> if id' = id then tau' else lookup gamma' id;;
end;;


(* Typechecking *)
exception Type_Error;;

let rec typeof (gamma: TEnv.t) (e: expression): ttype =
  match e with
      Const c ->
	(match c with
	    Unit -> TUNIT
	  | Bool _ -> TBOOL
	  | Int _ -> TINT
	  | Aop _ -> TARROW (TINT, TARROW (TINT, TINT))
	  | Rop _ -> TARROW (TINT, TARROW (TINT, TBOOL)))
    | Id id -> TEnv.lookup gamma id
    | App (e1, e2) ->
	let tau1 = typeof gamma e1 and tau2 = typeof gamma e2 in
	  (match tau1 with
	      TARROW (tau1', tau1'') -> if tau1' = tau2 then tau1'' else raise Type_Error
	    | _ -> raise Type_Error)
    | Cond (e0, e1, e2) ->
	let tau0 = typeof gamma e0 and tau1 = typeof gamma e1 and tau2 = typeof gamma e2 in
	  if tau0 = TBOOL && tau1 = tau2 then tau1 else raise Type_Error
    | Let (id, e1, e2) ->
	let tau1 = typeof gamma e1 in
	  typeof (TEnv.extend gamma id tau1) e2
    | Abstr (id, tau, e) ->
	let tau' = typeof (TEnv.extend gamma id tau) e in
	  TARROW (tau, tau')
    | Rec (id, tau, e) ->
	if tau = (typeof (TEnv.extend gamma id tau) e) then tau else raise Type_Error;;


(* Beispiel: let f = lambda x:bool.if x then 1 else 0 in f true *)
let e = Let ("f", Abstr ("x", TBOOL, Cond (Id ("x"), Const (Int 1), Const (Int 0))), App (Id ("f"), Const (Bool true))) in
  typeof TEnv.empty e;;
