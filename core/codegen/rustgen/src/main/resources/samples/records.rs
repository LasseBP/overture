use codegen_runtime::*;

/* types */
 #[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct foo {
		pub value: u64, 
		pub val2: char, 
	}
impl_record! { foo: value as u64,  val2 as char }

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct records;
impl records {
/* operations */
		pub fn create_and_use_record(&mut self) -> i64 {
			let mut f: foo = foo::new(1337, 'C');
			return 	f.value;
	
} 
		 pub fn new() -> records {
			let  instance: records = 	records::default();
			return instance;
	
} 
		
/* functions */
	}

impl Default for records {
	fn default() -> records {
				
		records
			}
}
	

	
