open Genlex 
open Split

let lexer = make_lexer ["("; ")"; "*"; "<"; ">"; "|"]

let concat = function [] -> failwith "concat []" 
  | head :: tail -> List.fold_left (fun a b -> Concat (a, b)) head tail

let parse_label stream = match Stream.next stream with
  | Ident s -> s
  | _ -> failwith "incorrect label" 

let error_of_token = function
  | Ident s -> failwith ("unexpected ident "^s)
  | Int i -> failwith ("unexpected int "^(string_of_int i))
  | Float f -> failwith ("unexpected float "^(string_of_float f))
  | String s -> failwith ("unexpected string "^s)
  | Char c -> failwith (Printf.sprintf "unexpected char %c" c)
  | Kwd k -> failwith ("invalid keyword "^k)

(* grammar: T = (T) / T* / <label / >label / T|T / T T*)
let rec parse_start stream token = match token with
  | Kwd "(" -> parse_until_close stream
  | Kwd "<" -> Label (Before (parse_label stream)) 
  | Kwd ">" -> Label (After (parse_label stream))
  | x -> error_of_token x
and parse_after already_parsed stream = match Stream.peek stream with
  | Some (Kwd "*") -> Stream.junk stream; 
      parse_after (Repeat already_parsed) stream 
  | Some (Kwd "|") -> 
    Stream.junk stream; 
    let t = parse_expr [] stream in 
    parse_after (Alt (already_parsed, t)) stream
  | _ -> already_parsed  
and parse_until_close stream = 
  let t = parse_expr [] stream in 
  match Stream.peek stream with  
  | Some (Kwd ")") -> Stream.junk stream; t
  | _ -> failwith "expecting closing ')'"
and parse_expr accu stream = match Stream.peek stream with
  | None | Some (Kwd ")") -> concat (List.rev accu)
  | Some token ->
    Stream.junk stream;
    let start = parse_start stream token in
    (* todo repeat *)
    let after = parse_after start stream in  
    parse_expr (after :: accu) stream

let parse string = 
  Stream.of_string string |> lexer |> parse_expr []
