
let sh_to_ml line =
    "  Unix.open_process_in (\"bash -c \\\""^String.escaped line^"\\\"\")"

let transform_next_line stream = match Util.stream_next stream with
  | Some line when Util.starts_with line "#!/bin" -> 
      Some "#use \"topfind\";; #thread;; #require \"core.top\";;"

  | Some line when Util.starts_with line "#" -> Some ("(*" ^ line ^ "*)")     
  | Some line when Util.starts_with line "let " 
      || Util.starts_with line "  "  || line = "" -> Some (line)
  | Some line -> Some (sh_to_ml line)
  | None -> None 

let transform_stream stream =
    Stream.from (fun _ -> transform_next_line stream)

let rec write_stream out_channel stream = match Util.stream_next stream with
  | None -> ()
  | Some line -> 
    output_string out_channel (line ^ "\n");
    write_stream out_channel stream

let transform_channel in_channel out_channel = 
  Util.line_stream in_channel |> transform_stream |> write_stream out_channel
