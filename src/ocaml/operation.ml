type argument_type = StringT | NumberT
type argument_value = StringArg of string | NumberArg of int
type value = String of string | Number of int
type signature = { name: string; argument_types: argument_type list; 
    implementation: argument_value list -> value -> value}
type t = {signature: signature; argument_values: argument_value list}

let value_to_string = function String s -> s | Number i -> string_of_int i

let substring arguments value = match arguments, value with
  | NumberArg i :: [], String s -> String (String.sub s 0 i)
  | _ -> failwith "unreachable" 

let signatures = 
  [{name="substring"; argument_types=[NumberT]; implementation=substring}]

let rec parse_arguments accu = function
  | [], lexems -> List.rev accu, lexems 
  | StringT :: tail_left, s :: tail_right -> 
    parse_arguments (StringArg s :: accu) (tail_left, tail_right)
  | NumberT :: tail_left, s :: tail_right -> 
    parse_arguments (NumberArg (int_of_string s) :: accu) (tail_left, tail_right)
  | _ -> failwith "invalid arguments"

let rec parse_lexems accu = function
  | [] -> accu
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
