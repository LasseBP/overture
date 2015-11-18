use codegen_runtime::*;
use quotes;
use Test1_mod::Test1;

// types
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
pub struct Plant {
    alarms: Set<Alarm>,
    schedule: Map<Period, Set<Expert>>,
}

impl Plant {
    // operations
    pub fn ExpertToPage(&mut self, a: Alarm, p: Period) -> Expert {
        assert!(self.pre_ExpertToPage(a.clone(), p.clone()));
        {
            let expert: Expert = self.schedule
                                     .get(p.clone())
                                     .be_such_that(|expert: Expert| -> bool {
                                         expert.quali.in_set(a.reqQuali.clone())
                                     });
            return expert;
        }


    }

    pub fn NumberOfExperts(&mut self, p: Period) -> u64 {
        assert!(self.pre_NumberOfExperts(p.clone()));
        return self.schedule.get(p.clone()).card();

    }

    pub fn ExpertIsOnDuty(&mut self, ex: Expert) -> Set<Period> {
        return self.schedule.domain().set_compr(|p: Period| -> bool {
                                                    self.schedule.get(p.clone()).in_set(ex.clone())
                                                },
                                                |p: Period| -> Period { p.clone() });
    }

    pub fn new(als: Set<Alarm>, sch: Map<Period, Set<Expert>>) -> Plant {
        assert!(self.pre_Plant(als.clone(), sch.clone()));
        let instance: Plant = Plant::default();
        return instance;


    }

    pub fn AddExpertToSchedule(&mut self, p: Period, ex: Expert) -> () {
        let arg0: Period = p.clone();
        let arg1: Set<Expert> = if self.schedule.domain().in_set(p.clone()) {
            self.schedule.get(p.clone()).union(set!(ex.clone()))
        } else {
            set!(ex.clone())
        };
        self.schedule.insert(arg0.clone(), arg1.clone());

    }

    pub fn RemoveExpertFromSchedule(&mut self, p: Period, ex: Expert) -> () {
        assert!(self.pre_RemoveExpertFromSchedule(p.clone(), ex.clone()));
        let exs: Set<Expert> = self.schedule.get(p.clone());
        self.schedule = if exs.card() == 1 {
            self.schedule.dom_restrict_by(set!(p.clone()))
        } else {
            self.schedule.ovrride(map!(p.clone() => exs.difference(set!(ex.clone()))))
        };


    }

    fn pre_ExpertToPage(&self, a: Alarm, p: Period) -> bool {
        return self.alarms.in_set(a.clone()) && self.schedule.domain().in_set(p.clone());
    }

    fn pre_NumberOfExperts(&self, p: Period) -> bool {
        return self.schedule.domain().in_set(p.clone());
    }

    fn pre_Plant(&self, als: Set<Alarm>, sch: Map<Period, Set<Expert>>) -> bool {
        return Plant::PlantInv(als.clone(), sch.clone());
    }

    fn pre_RemoveExpertFromSchedule(&self, p: Period, ex: Expert) -> bool {
        return self.schedule.domain().in_set(p.clone()) &&
               self.alarms.forall(|a: Alarm| -> bool {
            self.schedule
                .get(p.clone())
                .difference(set!(ex.clone()))
                .exists(|expert: Expert| -> bool { expert.quali.in_set(a.reqQuali.clone()) })
        });
    }


    // functions
    fn PlantInv(als: Set<Alarm>, sch: Map<Period, Set<Expert>>) -> bool {
        sch.domain().forall(|p: Period| -> bool { sch.get(p.clone()) != set!() }) &&
        als.forall(|a: Alarm| -> bool {
            sch.domain().forall(|p: Period| -> bool {
                sch.get(p.clone())
                   .exists(|expert: Expert| -> bool { expert.quali.in_set(a.reqQuali.clone()) })
            })
        })
    }
	}

impl Default for Plant {
    fn default() -> Plant {
        let alarms: Set<Alarm> = Default::default();
        let schedule: Map<Period, Set<Expert>> = Default::default();

        Plant {
            alarms: alarms,
            schedule: schedule,
        }
    }
}
