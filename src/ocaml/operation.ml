type argument_type = StringArgType | NumberArgType
type argument_value = StringArg of string | NumberArg of int

(** Values are inputs and output of strem operations *)
type value = StringValue of string | NumberValue of int | ListValue of value list
type value_type = StringType | NumberType | ListType of value_type

type signature = { 
  name: string; argument_types: argument_type list;
  input_type: value_type; output_type: value_type; 
  implementation: argument_value list -> value -> value}
type t = {
  signature: signature; argument_values: argument_value list;}

let rec value_to_string = function StringValue s -> s | NumberValue i -> string_of_int i 
  | ListValue l -> List.map value_to_string l |> Util.join ~separator:"; " |> fun x -> "[" ^ x ^ "]" 

let substring arguments value = match arguments, value with
  | NumberArg i :: [], StringValue s -> StringValue (String.sub s 0 i)
  | _ -> failwith "unreachable" 

let concat arguments value = match arguments, value with
  | StringArg arg :: [], StringValue s -> StringValue (s ^ arg)
  | _ -> failwith "unreachable" 

let prefix_with arguments value = match arguments, value with
  | StringArg arg :: [], StringValue s -> StringValue (arg ^ s)
  | _ -> failwith "unreachable" 

let index_of arguments value = match arguments, value with
  | StringArg s :: [], StringValue t -> NumberValue (try Str.search_forward (Str.regexp s) t 0 with Not_found -> -1)
  | _ -> failwith "unreachable" 

let to_string arguments value = match arguments, value with
  | [], NumberValue n -> StringValue (string_of_int n)
  | _ -> failwith "unreachable" 

let length arguments value = match arguments, value with
  | [], StringValue s -> NumberValue (String.length s)
  | _ -> failwith "unreachable" 

let replace_all arguments value = match arguments, value with
  | StringArg replace :: StringArg by :: [], StringValue s -> StringValue (Str.global_replace (Str.regexp replace) by s)
  | _ -> failwith "unreachable" 

let split arguments value = match arguments, value with
  | StringArg separator :: [], StringValue s when String.length separator = 1 -> 
    ListValue (String.split_on_char separator.[0] s |> List.map (fun s -> StringValue s))
  | _ -> failwith "unreachable" 

let join arguments value = match arguments, value with
  | StringArg separator :: [], ListValue l -> 
    l |> List.map (function StringValue s -> s | _ -> failwith "unreachable") |> Util.join ~separator |> (fun x -> StringValue x)
  | _ -> failwith "unreachable" 

let signatures = [
  {name="concat"; argument_types=[StringArgType]; implementation=concat; input_type=StringType; output_type=StringType};
  {name="index_of"; argument_types=[StringArgType]; implementation=index_of; input_type=StringType; output_type=NumberType};
  {name="substring"; argument_types=[NumberArgType]; implementation=substring; input_type=StringType; output_type=StringType};
  {name="to_string"; argument_types=[]; implementation=to_string; input_type=NumberType; output_type=StringType};
  {name="length"; argument_types=[]; implementation=length; input_type=StringType; output_type=NumberType};
  {name="replace_all"; argument_types=[StringArgType; StringArgType]; implementation=replace_all; input_type=StringType; output_type=StringType};
  {name="split"; argument_types=[StringArgType]; implementation=split; input_type=StringType; output_type= ListType StringType};
  {name="join"; argument_types=[StringArgType]; implementation=join; input_type=ListType StringType; output_type= StringType};
  {name="prefix_with"; argument_types=[StringArgType]; implementation=prefix_with; input_type=StringType; output_type= StringType};
  ]

let rec parse_arguments accu = function
  | [], lexems -> List.rev accu, lexems 
  | StringArgType :: tail_left, s :: tail_right -> 
    parse_arguments (StringArg s :: accu) (tail_left, tail_right)
  | NumberArgType :: tail_left, s :: tail_right -> 
    parse_arguments (NumberArg (int_of_string s) :: accu) (tail_left, tail_right)
  | _ -> failwith "invalid arguments"

let rec parse_lexems accu = function
  | [] -> List.rev accu
  | [""] -> List.rev accu
  | head :: tail -> 
    let signature = List.find (fun x -> x.name = head) signatures in
    let argument_values, remainder = parse_arguments [] (signature.argument_types, tail) in 
    parse_lexems ({signature; argument_values}::accu) remainder

let apply_to_elements ~filter ~f_matching ~f_non_matching list =
  let rec aux start_index accu = function 
    | [] -> List.rev accu
    | head :: tail -> 
      let element = if filter start_index then f_matching head else f_non_matching head in
      aux (start_index + 1) (element :: accu) tail
  in aux 0 [] list

let lexify string = 
  String.split_on_char '"' string
  |> apply_to_elements ~filter:(fun i -> i mod 2 = 0) 
      ~f_matching:(fun s -> String.trim s |> String.split_on_char ' ')
      ~f_non_matching:(fun x -> [x])
  |> List.flatten
  
let parse string = 
  lexify string |> parse_lexems []

let evaluate_one command value_list =
  List.map (command.signature.implementation command.argument_values) value_list

let evaluate commands value_list =
  List.fold_left (fun values command -> evaluate_one command values) value_list commands 

let starts_with prefix string =
  String.length prefix <= String.length string && String.equal (String.sub string 0 (String.length prefix)) prefix

let suggest_command prefix =
  List.filter (fun x -> starts_with prefix x.name) signatures
  