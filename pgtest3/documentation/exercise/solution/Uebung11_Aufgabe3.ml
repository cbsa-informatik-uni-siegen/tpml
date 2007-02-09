(* Uebungsblatt 11 / Aufgabe 3 *)

(* O'Caml braucht expliziten rekursiven Typ *)
type int_stream = St of (unit -> int * int_stream);;

let hd (St s: int_stream) = fst (s ());;
let tl (St s: int_stream) = snd (s ());;

(* nimmt die ersten 100 Elemente (als Liste) aus dem stream *)
let rec take n (s: int_stream) = if n = 0 then [] else hd s :: take (n - 1) (tl s);;

(* generiert stream von nat. Zahlen beginnend bei n *)
let rec nats_from n = 
 St (fun () -> n, nats_from (n + 1));;

(* filter s p ist der stream aller Elemente aus s, die die Eigenschaft p erfuellen *)
let rec filter p (s: int_stream) = 
  let x = hd s and s' = tl s in 
    if p x then St (fun () -> (x, filter p s')) else filter p s';;

(* map f s ist der stream, der aus s entsteht, indem man
   die Funktion map auf jedes Element von s anwendet *)
let rec map f (s: int_stream) = 
  let x = hd s and s' = tl s in 
    St (fun () -> (f x, map f s'));;

(* Aufgabe 3a: den stream aller nat. Zahlen, die nicht durch 3 teilbar sind *)
let ndiv3 = filter (fun n -> n mod 3 <> 0) (nats_from 0);;
take 100 ndiv3;;

(* Aufgabe 3b: den stream aller Quadratzahlen *)
let squares = map (fun n -> n * n) (nats_from 0);;
take 100 squares;;

(* Aufgabe 3c: den stream aller Primzahlen,
   vgl. "Sieb des Eratosthenes" *)
let rec sieve (s: int_stream) = 
  let p = hd s and s' = tl s in 
    St (fun () -> (p, sieve (filter (fun x -> x mod p <> 0) s')));; 
take 10 (sieve (nats_from 2));;
