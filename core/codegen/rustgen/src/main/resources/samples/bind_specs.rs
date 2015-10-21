use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct bind_specs;
impl bind_specs {
/* operations */
    pub fn op1(&mut self) -> Set<u64> {
        return set!{1, 2, 3}
                   .set_compr(|z: u64| -> bool { true }, |z: u64| -> u64 { z * 2 })
                   .clone();
    }
    pub fn op2(&mut self) -> Set<u64> {
        return cartesian_set!(set!{1, 2, 3}, set!{1, 2, 3})
                   .set_compr(|(z, y): (u64, u64)| -> bool { true },
                              |(z, y): (u64, u64)| -> u64 { z * y })
                   .clone();
    }
    pub fn new() -> bind_specs {
        let instance: bind_specs = bind_specs::default();
        return instance;

    }

/* functions */
	}

impl Default for bind_specs {
    fn default() -> bind_specs {

        bind_specs
    }
}
