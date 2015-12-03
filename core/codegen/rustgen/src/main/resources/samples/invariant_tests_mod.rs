use codegen_runtime::*;
use ClassT_mod::ClassT;

// types
pub type StringSeq = Seq<char>;
pub fn inv_StringSeq(s: Seq<char>) -> bool {
    s.len() < 20
}

pub type RGBColor = (u64, u64, u64);
pub fn inv_RGBColor((r, g, b): (u64, u64, u64)) -> bool {
    r <= 255 && g <= 255 && b <= 255
}

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct Account1 {
    pub balance: i64,
    pub owner: StringSeq,
}
impl_record! { Account1: balance as i64,  owner as StringSeq }
pub fn inv_Account1(Account1 { balance: bal, owner: owner }: Account1) -> bool {
    bal > -1000
}

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct Account2 {
    pub balance: i64,
    pub owner: StringSeq,
}
impl_record! { Account2: balance as i64,  owner as StringSeq }
pub fn inv_Account2(acc: Account2) -> bool {
    acc.balance > -2000
}

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct invariant_tests {
    owner: StringSeq,
    acc1: Account1,
    acc2: Account2,
}

impl invariant_tests {
    // operations
    pub fn cg_initinvariant_tests_1(&mut self) -> () {
        self.acc1 = Account1::new(-1500, self.owner.clone());
        self.acc2 = Account2::new(-1000, self.owner.clone());
        self.acc1.balance = 200;

    }

    pub fn new() -> invariant_tests {
        let mut instance: invariant_tests = invariant_tests::default();
        instance.cg_initinvariant_tests_1();
        return instance;

    }


    // functions

    fn inv_invariant_tests(&self) -> bool {
        return self.acc1.balance > self.acc2.balance &&
               self.acc1.owner.clone() == self.acc2.owner.clone();
    }
	}



impl Default for invariant_tests {
    fn default() -> invariant_tests {
        let owner: StringSeq = strseq!("owner");
        let acc1: Account1 = Default::default();
        let acc2: Account2 = Default::default();

        invariant_tests {
            owner: owner,
            acc1: acc1,
            acc2: acc2,
        }
    }
}
