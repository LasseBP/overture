use codegen_runtime::*;
use quotes;
use Plant_mod::Plant;

/* types */
/* values */
lazy_static! {
		 static ref p1: ::Plant_mod::Period = Token::new(&strseq!("Monday day"));
		 static ref p2: ::Plant_mod::Period = Token::new(&strseq!("Monday night"));
		 static ref p3: ::Plant_mod::Period = Token::new(&strseq!("Tuesday day"));
		 static ref p4: ::Plant_mod::Period = Token::new(&strseq!("Tuesday night"));
	}

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct Test1  {
		 a1: ::Plant_mod::Alarm,
		 a2: ::Plant_mod::Alarm,
		 ex1: ::Plant_mod::Expert,
		 ex2: ::Plant_mod::Expert,
		 ex3: ::Plant_mod::Expert,
		 ex4: ::Plant_mod::Expert,
		 plant: Plant,
	}

impl Test1 {
/* operations */
		pub fn Run(&mut self) -> (Set<::Plant_mod::Period>, ::Plant_mod::Expert) {
			let  periods: Set<::Plant_mod::Period> = self.plant.ExpertIsOnDuty(self.ex1.clone());
		let  expert: ::Plant_mod::Expert = self.plant.ExpertToPage(self.a1.clone(), ::Test1_mod::p1.clone());
			return (periods.clone(), expert.clone());
	
} 
	
		pub fn new() -> Test1 {
			let  instance: Test1 = Test1::default();
			return instance;
	
} 
	
		
/* functions */
	}

impl Default for Test1 {
	fn default() -> Test1 {
				let a1: ::Plant_mod::Alarm = ::Plant_mod::Alarm::new(strseq!("Mechanical fault"), ::Plant_mod::Qualification::from(quotes::Mech));
				let a2: ::Plant_mod::Alarm = ::Plant_mod::Alarm::new(strseq!("Tank overflow"), ::Plant_mod::Qualification::from(quotes::Chem));
				let ex1: ::Plant_mod::Expert = ::Plant_mod::Expert::new(set!(::Plant_mod::Qualification::from(quotes::Mech), ::Plant_mod::Qualification::from(quotes::Bio)));
				let ex2: ::Plant_mod::Expert = ::Plant_mod::Expert::new(set!(::Plant_mod::Qualification::from(quotes::Elec)));
				let ex3: ::Plant_mod::Expert = ::Plant_mod::Expert::new(set!(::Plant_mod::Qualification::from(quotes::Chem), ::Plant_mod::Qualification::from(quotes::Bio), ::Plant_mod::Qualification::from(quotes::Mech)));
				let ex4: ::Plant_mod::Expert = ::Plant_mod::Expert::new(set!(::Plant_mod::Qualification::from(quotes::Elec), ::Plant_mod::Qualification::from(quotes::Chem)));
				let plant: Plant = Plant::new(set!(a1.clone()), map!(::Test1_mod::p1.clone() => set!(ex1.clone(), ex4.clone()), ::Test1_mod::p2.clone() => set!(ex2.clone(), ex3.clone())));
				
		Test1
				{
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
	

	
