type t = { warning: string list; info: string list; error: string list}

type 'a with_log = {result : 'a; log:t}

let empty = {warning=[]; info=[]; error=[]}
let warn string_list = {empty with warning=string_list}
let info string_list = {empty with info=string_list}
let error string_list = {empty with error=string_list}
let concat log1 log2 = {warning=log1.warning @ log2.warning;
                        info=log1.info @ log2.info;
                        error=log1.error @ log2.error}

let to_stdout log =
  List.iter (fun x -> Printf.printf "[INFO] %s\n" x) log.info;
  List.iter (fun x -> Printf.printf "[WARN] %s\n" x) log.warning;
  List.iter (fun x -> Printf.printf "[ERROR] %s\n" x) log.error
  
let return result log = {result;log}

let bind with_log f = {result=f (with_log.result); log=with_log.log}
