let test =
  let open Streams in
  let output = 
    empty ()
    |> exec "git diff --name-only origin/main"
    ||> "sed \"s|/.*||\""
    ||> "uniq"
  in
  print_endline "===== ERR =====";
  write output.err ~to_channel:stdout;
  print_endline "=== END ERR ===";
  let join = accumulate output.out 
    ~f:(fun accu s -> accu ^ "," ^ s)
  in
  match join with None -> print_endline "empty diff"
  | Some j -> print_endline j
