let line_stream in_channel =
  Stream.from (fun _ ->
    try Some (input_line in_channel) with End_of_file -> None)

let rec write_stream out_channel stream = match Util.stream_next stream with
  | None -> ()
  | Some line -> 
    output_string out_channel (line ^ "\n");
    write_stream out_channel stream
