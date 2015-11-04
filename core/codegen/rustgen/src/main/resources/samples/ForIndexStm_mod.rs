use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct ForIndexStm ;
impl ForIndexStm {
/* operations */
		pub fn op(&mut self) -> () {
				{
	let mut i = 1;
	while i < 10 {
		/* skip */
		i += 5;
	}
}

		{
	let mut j = 10;
	while j < -1 {
		/* skip */
		j += -2;
	}
}

		{
	let mut i = 1;
	while i < 1 {
		/* skip */
		i += 1;
	}
}

		for i in (1..0) {
	/* skip */
}

	
} 
	
		pub fn new() -> ForIndexStm {
			let  instance: ForIndexStm = ForIndexStm::default();
			return instance;
	
} 
	
		
/* functions */
	}

impl Default for ForIndexStm {
	fn default() -> ForIndexStm {
				
		ForIndexStm
			}
}
	

	
