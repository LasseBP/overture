use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct expl_self_and_scopes {
		 kek: i64, 
	}

impl expl_self_and_scopes {
/* operations */
		pub fn cg_init_expl_self_and_scopes_1(&mut self) -> () {
			let mut i: i64 = 123;
		let mut a: expl_self_and_scopes = self;
			self.voidOp();
		self.voidOp();
		i = 		self.fiveop(self.i);
		i = 	self.fiveop(self.i);
		self.kek = 	expl_self_and_scopes::five(self.kek);
		self.kek = 		self.five(self.kek);
			a.kek = 	expl_self_and_scopes::five(	self.a.kek);
	
} 
		 pub fn new() -> expl_self_and_scopes {
			let mut instance: expl_self_and_scopes = 	expl_self_and_scopes::default();
			instance.cg_init_expl_self_and_scopes_1();
		return instance;
	
} 
				pub fn fiveop(&mut self, i: i64) -> i64 {
	return 2 + 3;
} 
		pub fn voidOp(&mut self) -> () {
	/* skip */
} 
		
/* functions */
		pub fn five(i: i64) -> i64 {
	2 + 3
} 
	}

impl Default for expl_self_and_scopes {
	fn default() -> expl_self_and_scopes {
				let kek: i64 = Default::default();
				
		expl_self_and_scopes
				{
						kek: kek,
					}
			}
}
	

	
