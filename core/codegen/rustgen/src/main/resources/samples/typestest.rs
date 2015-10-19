use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct foo {
    pub value: u64,
    pub val2: char,
}
impl_record! { foo: value as u64,  val2 as char }

pub type StringSeq = Seq<char>;

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct typestest;
impl typestest {
/* operations */
    pub fn new() -> typestest {
        let instance: typestest = typestest::default();
        return instance;

    }

/* functions */
	}

impl Default for typestest {
    fn default() -> typestest {

        typestest
    }
}
