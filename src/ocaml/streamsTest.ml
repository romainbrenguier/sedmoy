let test =
  let output = 
    Streams.empty ()
    |> Streams.exec "git diff --name-only origin/main"
  in
  Streams.write output.out ~to_channel:stdout;
  print_endline "===== ERR =====";
  Streams.write output.err ~to_channel:stdout
