use codegen_runtime::*;

// types
// values
lazy_static! {
		 static ref p1: ::Plant_cg_mod::Period = Token::new(strseq!("Monday day"));
		 static ref p2: ::Plant_cg_mod::Period = Token::new(strseq!("Monday night"));
		 static ref p3: ::Plant_cg_mod::Period = Token::new(strseq!("Tuesday day"));
		 static ref p4: ::Plant_cg_mod::Period = Token::new(strseq!("Tuesday night"));
	}

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct Test1 {
    a1: ::Plant_cg_mod::Alarm,
    a2: ::Plant_cg_mod::Alarm,
    ex1: ::Plant_cg_mod::Expert,
    ex2: ::Plant_cg_mod::Expert,
    ex3: ::Plant_cg_mod::Expert,
    ex4: ::Plant_cg_mod::Expert,
    plant: Plant,
}

impl Test1 {
    // operations
    pub fn Run(&mut self) -> (Set<::Plant_cg_mod::Period>, ::Plant_cg_mod::Expert) {
        let periods: Set<::Plant_cg_mod::Period> = self.plant.ExpertIsOnDuty(self.ex1.clone());
        let expert: ::Plant_cg_mod::Expert = self.plant.ExpertToPage(self.a1.clone(),
                                                                     ::Test1_cg_mod::p1.clone());
        return (periods.clone(), expert.clone());

    }

    pub fn Test1() -> Test1 {
        let instance: Test1 = Test1::default();
        return instance;

    }


// functions
	}

impl Default for Test1 {
    fn default() -> Test1 {
        let a1: ::Plant_cg_mod::Alarm =
            Alarm::new(strseq!("Mechanical fault"),
                       ::Plant_cg_mod::Qualification::from(quotes::Mech));
        let a2: ::Plant_cg_mod::Alarm =
            Alarm::new(strseq!("Tank overflow"),
                       ::Plant_cg_mod::Qualification::from(quotes::Chem));
        let ex1: ::Plant_cg_mod::Expert = Expert::new(set!(Any::from(quotes::Mech),
                                                           Any::from(quotes::Bio)));
        let ex2: ::Plant_cg_mod::Expert = Expert::new(set!(quotes::Elec));
        let ex3: ::Plant_cg_mod::Expert = Expert::new(set!(Any::from(quotes::Chem),
                                                           Any::from(quotes::Bio),
                                                           Any::from(quotes::Mech)));
        let ex4: ::Plant_cg_mod::Expert = Expert::new(set!(Any::from(quotes::Elec),
                                                           Any::from(quotes::Chem)));
        let plant: Plant = Plant::new(set!(a1.clone()), map!(::Test1_cg_mod::p1.clone() => set!(ex1.clone(), ex4.clone()), ::Test1_cg_mod::p2.clone() => set!(ex2.clone(), ex3.clone())));

        Test1 {
            a1: a1,
            a2: a2,
            ex1: ex1,
            ex2: ex2,
            ex3: ex3,
            ex4: ex4,
            plant: plant,
        }
    }
}
