use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct sets;
impl sets {
/* operations */
    pub fn createSet(&mut self) -> Set<i64> {
        return set!{1, 2, 3};
    }
    pub fn new() -> sets {
        let instance: sets = sets::default();
        return instance;

    }

/* functions */
	}

impl Default for sets {
    fn default() -> sets {

        sets
    }
}