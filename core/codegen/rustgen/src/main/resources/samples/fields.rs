use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct fields {
		 val: u64, 
		 kek: i64, 
		pub s: u64, 
	}

impl fields {
/* operations */
		 pub fn new() -> fields {
			let  instance: fields = 	fields::default();
			return instance;
	
} 
		
/* functions */
	}

impl Default for fields {
	fn default() -> fields {
				let val: u64 = 1;
				let kek: i64 = 1337;
				let s: u64 = 123;
				
		fields
				{
						val: val,
						kek: kek,
						s: s,
					}
			}
}
	

	
