use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct bind_specs ;
impl bind_specs {
/* operations */
		pub fn set_comp1(&mut self) -> Set<u64> {
	return set!{1, 2, 3}.set_compr(|z: u64| -> bool { true } , |z: u64| -> u64 { z * 2 } );
} 
		pub fn set_comp2(&mut self) -> Set<u64> {
	return cartesian_set!(set!{1, 2, 3}, set!{1, 2, 3}).set_compr(|(z, y): (u64, u64)| -> bool { z == y } , |(z, y): (u64, u64)| -> u64 { z * y } );
} 
		pub fn map_comp1(&mut self) -> Map<u64,char> {
	return cartesian_set!(set!{1, 2, 3}, set!{'c', 'k'}).map_compr(|(z, y): (u64, char)| -> bool { true } , |(z, y): (u64, char)| -> (u64, char) { (z, y) } );
} 
		pub fn seq_comp1(&mut self) -> Seq<u64> {
	return set!{1, 2, 3}.seq_compr(|z: u64| -> bool { z > 1 } , |z: u64| -> u64 { z * 2 } ).clone();
} 
		pub fn exists_1(&mut self) -> bool {
	return cartesian_set!(set!{1, 2, 3}, set!{1, 2, 3}).exists(|(x, y): (u64, u64)| -> bool { x > y } );
} 
		pub fn exists1_1(&mut self) -> bool {
	return set!{1, 2, 3}.exists1(|x: u64| -> bool { x > 2 } );
} 
		pub fn forall_1(&mut self) -> bool {
	return cartesian_set!(set!{1, 2, 3}, set!{4, 5}).forall(|(x, y): (u64, u64)| -> bool { y > x } );
} 
		 pub fn new() -> bind_specs {
			let  instance: bind_specs = bind_specs::default();
			return instance;
	
} 
		
/* functions */
	}

impl Default for bind_specs {
	fn default() -> bind_specs {
				
		bind_specs
			}
}
	

	
