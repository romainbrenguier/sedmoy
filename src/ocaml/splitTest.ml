open Split

let test_label =
  expression_to_automaton epsilon_automaton (Label (Before "x"))
  |> automaton_to_string
  |> print_endline

let test_concat =
  Concat((Label (Before "x")), (Label (After "y")))
  |> expression_to_automaton epsilon_automaton
  |> automaton_to_string 
  |> print_endline

let test_alt =
  Alt((Label (Before "x")), Concat(Label (After "y"), Label(After "z")))
  |> expression_to_automaton epsilon_automaton
  |> automaton_to_string 
  |> print_endline

let test_repeat =
  Repeat(Concat(Label (After "y"), Label(After "z")))
  |> expression_to_automaton epsilon_automaton
  |> automaton_to_string 
  |> print_endline

let test_parser =
  SplitParser.parse "(>a)* | <b"
  |> expression_to_string
  |> print_endline