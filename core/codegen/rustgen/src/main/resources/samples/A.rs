use codegen_runtime::*;

/* types */
#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct R1 {
    pub x: u64,
}
impl_record! { R1: x as u64 }

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct R2 {
    pub r1: R1,
    pub x: u64,
}
impl_record! { R2: r1 as R1,  x as u64 }

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct A {
    a: R2,
    b: R2,
    c: R1,
    d: u64,
}

impl A {
/* operations */
    pub fn op1(&mut self) -> u64 {
        let mut r: R1 = R1::new(2);
        r.x = 3;
        return r.x;

    }
    pub fn op2(&mut self) -> u64 {
        let mut a: R1 = R1::new(5);
        let mut b: R1 = a.clone();
        return b.x;

    }
    pub fn op3(&mut self) -> u64 {
        let mut a: R2 = R2::new(R1::new(2), 3);
        let mut b: R1 = a.r1.clone();
        a.x = 1;
        a.r1.x = 2;
        return a.r1.x;

    }
    pub fn op4(&mut self) -> R2 {
        let mut a: R2 = R2::new(R1::new(2), 3);
        return a.clone();

    }
    pub fn op5(&mut self) -> R1 {
        let mut a: R2 = R2::new(R1::new(2), 3);
        return a.r1.clone();

    }
    pub fn new() -> A {
        let instance: A = A::default();
        return instance;

    }

/* functions */
	}

impl Default for A {
    fn default() -> A {
        let a: R2 = R2::new(R1::new(2), 3);
        let b: R2 = A::a.clone();
        let c: R1 = self.a.r1.clone();
        let d: u64 = self.a.r1.x;

        A {
            a: a,
            b: b,
            c: c,
            d: d,
        }
    }
}
