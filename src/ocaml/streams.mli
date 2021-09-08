type input = string Stream.t
type output = {out:string Stream.t; err:string Stream.t}

val next : 'a Stream.t -> 'a option

val of_channel : in_channel -> input

val write : out_channel -> input -> unit
