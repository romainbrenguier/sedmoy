open Ubiquitous

let test_parse_field_declaration =
  List.map (fun x -> 
      x
      |> parse_field_declaration
      |> function
        | None -> failwith "parsing failed"
        | Some field_declaration ->
           print_endline (field_declaration_to_string field_declaration)
    )
    [
      "public static final int FIELD;";
      "static final boolean $$;";
    ]


let test_parse_method_declaration =
  List.map (fun x -> 
      x
      |> parse_method_declaration
      |> function
        | None -> failwith "parsing failed"
        | Some field_declaration ->
           print_endline (method_declaration_to_string field_declaration)
    )
    [
      "public static final int method();";
      "static final boolean get();";
    ]
