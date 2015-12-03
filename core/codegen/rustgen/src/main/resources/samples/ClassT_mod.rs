use codegen_runtime::*;
use invariant_tests_mod::invariant_tests;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct ClassT ;
impl ClassT {
/* operations */
		pub fn new() -> ClassT {
			let  instance: ClassT = ClassT::default();
			return instance;
	
} 
	
		
/* functions */
		
		 fn inv_ClassT(&self) -> bool {
	return true;
} 
	}



impl Default for ClassT {
	fn default() -> ClassT {
				
		ClassT
			}
}
	

	
