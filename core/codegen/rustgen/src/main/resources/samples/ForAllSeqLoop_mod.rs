use codegen_runtime::*;

/* types */
 #[derive(PartialEq, Eq, Clone, Hash, Debug)]
 struct Rec {
		pub x: i64,
	}
impl_record! { Rec: x as i64 }

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct ForAllSeqLoop ;
impl ForAllSeqLoop {
/* operations */
		pub fn op(&mut self) -> () {
			let  x: Seq<Rec> = seq!(::ForAllSeqLoop_mod::Rec::new(1), ::ForAllSeqLoop_mod::Rec::new(2), ::ForAllSeqLoop_mod::Rec::new(3));
			for e in x.clone()) {
	{
			/* skip */
	}

}
	
} 
	
		pub fn op1(&mut self) -> () {
	for n in seq!(1, 2, 3)) {
	/* skip */
}
} 
	
		pub fn op2(&mut self) -> () {
	for e in seq!()) {
	/* skip */
}
} 
	
		pub fn op3(&mut self) -> () {
	for n in ForAllSeqLoop::f()) {
	/* skip */
}
} 
	
		pub fn op4(&mut self) -> () {
	for n in seq!(1, 2, 3).reverse()) {
	/* skip */
}
} 
	
		pub fn new() -> ForAllSeqLoop {
			let  instance: ForAllSeqLoop = ForAllSeqLoop::default();
			return instance;
	
} 
	
		
/* functions */
		pub fn f() -> Seq<u64> {
	seq!(1, 2, 3)
} 
	}

impl Default for ForAllSeqLoop {
	fn default() -> ForAllSeqLoop {
				
		ForAllSeqLoop
			}
}
	

	
