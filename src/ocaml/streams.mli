type input = string Stream.t
type output = {out:string Stream.t; err:string Stream.t}

val next : 'a Stream.t -> 'a option

val from : ('a -> ('b option * 'a)) -> 'a -> 'b Stream.t

val empty : unit -> 'a Stream.t

val concat : 'a Stream.t -> 'a Stream.t -> 'a Stream.t

val of_channel : in_channel -> input

val write : input -> to_channel:out_channel -> unit

val write_to_file : input -> file_name:string -> unit

val exec : string  -> input -> output

val pipe : output -> (input -> output) -> output
