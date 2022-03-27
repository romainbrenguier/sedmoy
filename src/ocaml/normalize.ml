

let replacement =
  [ "Ά","Α";
    "Έ","Ε";
    "Ή","Η";
    "Ί","Ι";
    "Ό","Ο";
    "Ύ","Υ";
    "Ώ","Ω";
    "Ϊ","Ι";
    "Ϋ","Υ";
    "ά","α";
    "έ","ε";
    "ή","η";
    "ί","ι";
    "ΰ","υ";
    "ϊ","ι";
    "ϋ","υ";
    "ό","ο";
    "ύ","υ";
    "ώ","ω";
    "ϐ","?";
    "ϑ","?";
    "ϒ","?";
    "ϓ","?";
    "ϔ","?";
    "ϕ","?";
    "ϖ","?";
    "ϗ","?";
    "Ϙ","?";
    "ϙ","?";
    "Ϛ","?";
    "ϛ","?";
    (* German *)
    "Ä", "a";
    "Ö", "o";
    "Ü", "u";
    "ä", "a";
    "ü", "u"  
  ]

let regexp_to_replace =
  let open List in
  let keys = map fst replacement in
  fold_left (fun x y -> x^"\\|"^y) (hd keys) (tl keys)
  |> Str.regexp 

let find string =
  try List.assoc string replacement
  with Not_found ->
    Log.error ["Could not find '"^string^"', replacing with '?'"] |> Log.to_stdout;
    "?"

(** Remove accents on utf8 strings, only works for greek at the moment *)
let replace s =
  let open Str in
  let accent_replaced =
    full_split regexp_to_replace s
    |> List.map (function Text t -> t | Delim x -> find x)
    |> List.fold_left (^) ""
  in
  Printf.sprintf "%c%s"
    (Char.lowercase_ascii (String.get accent_replaced 0))
    (String.sub accent_replaced 1 (String.length accent_replaced - 1))
    
