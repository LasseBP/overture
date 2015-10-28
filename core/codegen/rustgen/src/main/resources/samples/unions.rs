use codegen_runtime::*;

// types
#[derive(PartialEq, Eq, Clone, Hash)]
pub enum U {
    Ch0(quotes::A),
    Ch1(quotes::T),
}
impl_union! { U:
				quotes::A as U::Ch0, 
			quotes::T as U::Ch1 
	}

 type optional = Option<u64>;

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
struct kek {
    pub val: i64,
}
impl_record! { kek: val as i64 }

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct unions;
impl unions {
    // operations
    pub fn new() -> unions {
        let instance: unions = unions::default();
        return instance;

    }

    // functions
    pub fn f1(u: U) -> U {
        unions::f1(U::from(quotes::T))
    }
    pub fn f2(u: U) -> quotes::T {
        quotes::T::from(unions::f1(U::from(quotes::T)))
    }
    pub fn f3(i: u64) -> Option<u64> {
        Some(i)
    }
    pub fn f4(i: Option<u64>) -> u64 {
        i.expect("Optional was nil.")
    }
	}

impl Default for unions {
    fn default() -> unions {

        unions
    }
}
