type label =  Before of string | After of string 

let label_to_string = function After s -> ">"^s | Before s -> "<"^s

type expression = Label of label | Concat of expression * expression 
  | Repeat of expression | Alt of expression * expression 

let rec expression_to_string = function
  | Label l -> label_to_string l
  | Concat (a,b) -> expression_to_string a ^" "^ expression_to_string b
  | Repeat a -> "("^(expression_to_string a)^")*"
  | Alt (a,b) -> "("^(expression_to_string a)^"|"^(expression_to_string b)^")"

type state = int

type edge = state * label * state

type automaton = { initial: state; final: state; nb_states: int; edges: edge list }

let epsilon_automaton = { initial=1; final=1; nb_states=1; edges=[]}

let add_state auto : (state * automaton) = 
  let new_nb_state = auto.nb_states + 1 in 
    new_nb_state, { auto with nb_states = new_nb_state}

let add_edge auto edge = { auto with edges = edge :: auto.edges}
let merge_dest_states auto state1 state2 = 
  let edges = List.map (fun (src, l, dest) -> 
    if dest = state2 then (src, l, state1) else (src, l, dest))
    auto.edges
  in 
  let final = if auto.final = state2 then state1 else auto.final in
  {auto with edges; final}  

let rec expression_to_automaton accu_auto = function
  | Label label -> 
    let (state, auto_with_state) = add_state accu_auto in
    let edge = accu_auto.final, label, state in
    let auto_with_edge = add_edge auto_with_state edge in
    {auto_with_edge with final=state}
  | Concat (expr1, expr2) ->
    let auto1 = expression_to_automaton accu_auto expr1 in
    expression_to_automaton auto1 expr2
  | Alt (expr1, expr2) ->
    let auto1 = expression_to_automaton accu_auto expr1 in
    let final1 = auto1.final in
    let auto2 = 
      expression_to_automaton {auto1 with final=accu_auto.final} expr2 in
    merge_dest_states auto2 final1 auto2.final
  | Repeat expr ->
    let auto = expression_to_automaton accu_auto expr in
    merge_dest_states auto accu_auto.final auto.final
  
let edge_to_string (src, label, dest) = 
  Printf.sprintf "%d [%s] %d" src (label_to_string label) dest

let automaton_to_string auto =
  let edges_string = 
    auto.edges |> List.map edge_to_string |> Util.join ~separator:", " 
  in Printf.sprintf "initial:%d final:%d edges:{%s}"
    auto.initial auto.final edges_string
