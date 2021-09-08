(** stream of lines *)
type input = string Stream.t

(** streams of lines for output and errors *)
type output = {out:string Stream.t; err:string Stream.t}

let next stream =
  try Some (Stream.next stream) with Stream.Failure -> None

let of_channel in_channel =
  Stream.from (fun _ ->
    try Some (input_line in_channel) with End_of_file -> None)

let rec write out_channel stream = match next stream with
  | None -> ()
  | Some line -> 
    output_string out_channel (line ^ "\n");
    write out_channel stream
