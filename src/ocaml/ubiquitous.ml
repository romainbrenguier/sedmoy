(** This module aims at extracting the ubiquitous language starting from a compiled java class.
This is meant to read the output of the javap command. *)

type visibility = Public | Private | Protected
type java_class_name = Class of string
type field_declaration = {
    field_type : java_class_name;
    field_name: string;
    visibility: visibility option
  }

let field_declaration_to_string = function
  | {field_type=Class x; field_name; _} -> "field: " ^ x ^ " " ^ field_name ^ ";"
  
type method_declaration = {
    return_type : java_class_name;
    method_name: string;
    argument_types:java_class_name list
  } 

let method_declaration_to_string = function
  | {return_type=Class x; method_name; _} -> "method: " ^ x ^ " " ^ method_name ^ ";"
  
type java_class = {
    class_name : java_class_name;
    fields: field_declaration list;
    methods: method_declaration list}

let parse_field_declaration string =
  String.split_on_char ' ' string
  
  |> function
    | "public" :: "static" :: "final" :: type_name :: name :: [] ->
       Some {field_type=Class type_name; field_name=name; visibility=None}
    | _ -> None

             
let parse_method_declaration string =
  String.split_on_char ' ' string
  |> function
    | "public" :: "static" :: "final" :: type_name :: name :: "(" :: _ :: ")" :: [] ->
       Some {return_type=Class type_name; method_name=name; argument_types=[]}
    | _ -> None

