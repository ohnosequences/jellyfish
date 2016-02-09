package ohnosequences.jellyfish.api

case class uint64(private val input: BigInt) {

  // NOTE: it is important not to pass to jellyfish an invalid value,
  //  so we take abs of the passes BigInt and compare it to the MaxValue
  def value: BigInt =
    uint64.MaxValue.input.min(input.abs)
    // NOTE: .abs is arguable, probably this is a more intuitive convention:
    // uint64.MaxValue.input.min(
    //   uint64.MinValue.input.max(
    //     input
    //   )
    // )

  override def toString = this.value.toString
}

case object uint64 {

  val MinValue: uint64 = uint64(0)
  val MaxValue: uint64 = uint64(BigInt(Long.MaxValue) * 2)
}
