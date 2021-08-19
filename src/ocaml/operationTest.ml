let test =
  let result = Operation.lexify "foo \"bar \" baz quz" in
  List.iter (fun x -> print_endline ("["^x^"]")) result
