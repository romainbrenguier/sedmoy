type input = string Stream.t
type output = {out:string Stream.t; err:string Stream.t}

val next : 'a Stream.t -> 'a option

val from : ('a -> ('b option * 'a)) -> 'a -> 'b Stream.t

val empty : unit -> 'a Stream.t

val single : 'a -> 'a Stream.t

val concat : 'a Stream.t -> 'a Stream.t -> 'a Stream.t

val add : 'a -> 'a Stream.t -> 'a Stream.t

val fold : 'a Stream.t -> f:('b -> 'a -> 'b) -> init:'b -> 'b

val accumulate : 'a Stream.t -> f:('a -> 'a -> 'a) -> 'a option

val of_channel : in_channel -> input

val write : input -> to_channel:out_channel -> unit

val write_to_file : input -> file_name:string -> unit

val exec : string -> input -> output

val exec_no_input : string -> output

val pipe : (input -> output) -> output -> output

val (||>) : output -> string -> output

val flush_err : output -> string Stream.t

val (|>|) : output -> (input -> 'a) -> 'a

val seq : output -> output -> output

val (|+) : output -> output -> output

val echo : string -> output
