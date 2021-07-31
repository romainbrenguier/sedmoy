type argument_type = StringT | NumberT
type value_type = StringValue | NumberValue | ListValue of value_type
type argument_value = StringArg of string | NumberArg of int
type value = String of string | Number of int
type signature = { 
  name: string; argument_types: argument_type list;
  input_type: value_type; output_type: value_type; 
  implementation: argument_value list -> value -> value}
type t = {
  signature: signature; argument_values: argument_value list;}

let value_to_string = function String s -> s | Number i -> string_of_int i

let substring arguments value = match arguments, value with
  | NumberArg i :: [], String s -> String (String.sub s 0 i)
  | _ -> failwith "unreachable" 

let concat arguments value = match arguments, value with
  | StringArg arg :: [], String s -> String (s ^ arg)
  | _ -> failwith "unreachable" 

let index_of arguments value = match arguments, value with
  | StringArg s :: [], String t -> Number (try Str.search_forward (Str.regexp s) t 0 with Not_found -> -1)
  | _ -> failwith "unreachable" 

let to_string arguments value = match arguments, value with
  | [], Number n -> String (string_of_int n)
  | _ -> failwith "unreachable" 

let length arguments value = match arguments, value with
  | [], String s -> Number (String.length s)
  | _ -> failwith "unreachable" 

let replace_all arguments value = match arguments, value with
  | StringArg replace :: StringArg by :: [], String s -> String (Str.global_replace (Str.regexp replace) by s)
  | _ -> failwith "unreachable" 

let signatures = [
  {name="concat"; argument_types=[StringT]; implementation=concat; input_type=StringValue; output_type=StringValue};
  {name="index_of"; argument_types=[StringT]; implementation=index_of; input_type=StringValue; output_type=NumberValue};
  {name="substring"; argument_types=[NumberT]; implementation=substring; input_type=StringValue; output_type=StringValue};
  {name="to_string"; argument_types=[]; implementation=to_string; input_type=NumberValue; output_type=StringValue};
  {name="length"; argument_types=[]; implementation=length; input_type=StringValue; output_type=NumberValue};
  {name="replace_all"; argument_types=[StringT; StringT]; implementation=replace_all; input_type=StringValue; output_type=StringValue};
  ]

let rec parse_arguments accu = function
  | [], lexems -> List.rev accu, lexems 
  | StringT :: tail_left, s :: tail_right -> 
    parse_arguments (StringArg s :: accu) (tail_left, tail_right)
  | NumberT :: tail_left, s :: tail_right -> 
    parse_arguments (NumberArg (int_of_string s) :: accu) (tail_left, tail_right)
  | _ -> failwith "invalid arguments"

let rec parse_lexems accu = function
  | [] -> List.rev accu
  | head :: tail -> 
    let signature = List.find (fun x -> x.name = head) signatures in
    let argument_values, remainder = parse_arguments [] (signature.argument_types, tail) in 
    parse_lexems ({signature; argument_values}::accu) remainder

let parse string = 
  String.split_on_char ' ' string |> parse_lexems []

let evaluate_one command value_list =
  List.map (command.signature.implementation command.argument_values) value_list

let evaluate commands value_list =
  List.fold_left (fun values command -> evaluate_one command values) value_list commands 
