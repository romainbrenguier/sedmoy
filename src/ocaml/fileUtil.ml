let rec read_channel accu in_channel = 
  let line = try Some (input_line in_channel) with End_of_file -> None in
  match line with None -> accu | Some l -> read_channel (l :: accu) in_channel

let read_file filename =
  let channel = open_in filename in
  let lines = read_channel [] channel |> List.rev in
  close_in channel;
  lines
