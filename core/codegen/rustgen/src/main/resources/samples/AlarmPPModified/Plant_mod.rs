use codegen_runtime::*;
use quotes;
use Test1_mod::Test1;

/* types */
pub type Period = Token;

pub type StringSeq = Seq<char>;

 #[derive(PartialEq, Eq, Clone, Hash)]
pub enum Qualification {
		Ch0(quotes::Bio),
		Ch1(quotes::Chem),
		Ch2(quotes::Elec),
		Ch3(quotes::Mech),
	}
impl_union! { Qualification:
				quotes::Bio as Qualification::Ch0, 
				quotes::Chem as Qualification::Ch1, 
				quotes::Elec as Qualification::Ch2, 
			quotes::Mech as Qualification::Ch3 
	}

 #[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct Alarm {
		pub descr: StringSeq,
		pub reqQuali: Qualification,
	}
impl_record! { Alarm: descr as StringSeq,  reqQuali as Qualification }

 #[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct Expert {
		pub quali: Set<Qualification>,
	}
impl_record! { Expert: quali as Set<Qualification> }

#[derive(PartialEq, Eq, Clone, Hash, Debug)]
pub struct Plant  {
		 alarms: Set<Alarm>,
		 schedule: Map<Period,Set<Expert>>,
	}

impl Plant {
/* operations */
				pub fn ExpertToPage(&mut self, a: Alarm, p: Period) -> Expert {
			let  expert: Expert = self.schedule.get(p.clone()).be_such_that(|expert: Expert| -> bool { expert.quali.in_set(a.reqQuali.clone()) } );
			return expert.clone();
	
} 
	
				pub fn NumberOfExperts(&mut self, p: Period) -> u64 {
	return self.schedule.get(p.clone()).card();
} 
	
				pub fn ExpertIsOnDuty(&mut self, ex: Expert) -> Set<Period> {
	return self.schedule.domain().set_compr(|p: Period| -> bool { self.schedule.get(p.clone()).in_set(ex.clone()) } , |p: Period| -> Period { p.clone() } );
} 
	
				pub fn cg_init_Plant_1(&mut self, als: Set<Alarm>, sch: Map<Period,Set<Expert>>) -> () {
				self.alarms = als.clone();
		self.schedule = sch.clone();
	
} 
	
			pub fn new(als: Set<Alarm>, sch: Map<Period,Set<Expert>>) -> Plant {
			let mut instance: Plant = Plant::default();
			{
		let  arg0: Set<Alarm> = als.clone();
		let  arg1: Map<Period,Set<Expert>> = sch.clone();
			instance.cg_init_Plant_1(arg0.clone(), arg1.clone());
	}

		return instance;
	
} 
	
				pub fn AddExpertToSchedule(&mut self, p: Period, ex: Expert) -> () {
			let  arg0: Period = p.clone();
		let  arg1: Set<Expert> = if self.schedule.domain().in_set(p.clone()) { set!(ex.clone()).union(self.schedule.get(p.clone())) } else { set!(ex.clone()) };
			self.schedule.insert(arg0.clone(), arg1.clone());
	
} 
	
				pub fn RemoveExpertFromSchedule(&mut self, p: Period, ex: Expert) -> () {
			let  exs: Set<Expert> = self.schedule.get(p.clone());
			self.schedule = if exs.card() == 1 { self.schedule.dom_restrict_by(set!(p.clone())) } else { self.schedule.ovrride(map!(p.clone() => set!(ex.clone()).difference(exs.clone()))) };
	
} 
	
		
/* functions */
		 fn PlantInv(als: Set<Alarm>, sch: Map<Period,Set<Expert>>) -> bool {
	sch.domain().forall(|p: Period| -> bool { sch.get(p.clone()) != set!() } ) && als.forall(|a: Alarm| -> bool { sch.domain().forall(|p: Period| -> bool { sch.get(p.clone()).exists(|expert: Expert| -> bool { expert.quali.in_set(a.reqQuali.clone()) } ) } ) } )
} 
	}

impl Default for Plant {
	fn default() -> Plant {
				let alarms: Set<Alarm> = Default::default();
				let schedule: Map<Period,Set<Expert>> = Default::default();
				
		Plant
				{
						alarms: alarms,
						schedule: schedule,
					}
			}
}
	

	
