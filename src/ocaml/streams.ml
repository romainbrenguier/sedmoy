(** stream of lines *)
type input = string Stream.t

(** streams of lines for output and errors *)
type output = {out:string Stream.t; err:string Stream.t}

let next stream =
  try Some (Stream.next stream) with Stream.Failure -> None

let from f initial_state =
  let state = ref initial_state in
  Stream.from (fun _ -> 
    let (result, new_state) = f !state in state := new_state; result)

let empty () = Stream.from (fun _ -> raise End_of_file)

let concat stream1 stream2 =
  let f i = if i = 1
    then 
      match next stream1 with None -> (next stream2, 2) | Some x -> (Some x, 1)
    else (next stream2, 2)
  in from f 1

let of_channel in_channel =
  Stream.from (fun _ ->
    try Some (input_line in_channel) with End_of_file -> None)

let rec write stream ~to_channel = match next stream with
  | None -> ()
  | Some line -> 
    output_string to_channel (line ^ "\n");
    write ~to_channel stream

let write_to_file stream ~file_name =
  let out_channel = open_out file_name in 
  try write stream ~to_channel:out_channel
  with _ -> ();
  close_out out_channel

let exec cmd input =
  let in_channel, out_channel, err_in_channel = Unix.open_process_full cmd [| |] in
  (try write input ~to_channel:out_channel with End_of_file -> ());
  close_out out_channel;
  {out=of_channel in_channel; err=of_channel err_in_channel}

let pipe output consumer =
  let new_output = consumer output.out in
  {out=new_output.out; err=concat output.err new_output.err}
