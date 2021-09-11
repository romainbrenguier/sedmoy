let test =
  let open Streams in
  let joined = 
    empty ()
    |> exec "git diff --name-only origin/main"
    ||> "sed \"s|/.*||\""
    ||> "uniq"
    |>| accumulate 
      ~f:(fun accu s -> accu ^ "," ^ s)
  in
  match joined with None -> print_endline "empty diff"
  | Some j -> print_endline j
