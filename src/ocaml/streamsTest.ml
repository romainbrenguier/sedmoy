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

let test_concat =
  let open Streams in
  let stream1 = single "foo" in
  let stream2 = single "bar" in
  let result = concat stream1 stream2 in
  write result ~to_channel:stdout

let test_seq =
  let open Streams in
  let result = seq (echo "FOO") (echo "BAR") in
  write ~to_channel:stdout result.out

let test_exec =
(** warning this takes 3 sec to execute *)
  flush stdout;
  let open Streams in
  let output = 
  echo "start" |+ exec_no_input "sleep 1"
  |+ echo "step" |+ exec_no_input "sleep 2"
  |+ echo "stop"
  in Streams.fold ~init:() 
    ~f:(fun () s -> print_endline s) output.out