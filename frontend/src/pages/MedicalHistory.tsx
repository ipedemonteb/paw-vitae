import { Card } from "@/components/ui/card.tsx";
import PatientProfileCard from "@/components/PatientProfileCard.tsx";
import {ChevronsUpDown, Info} from "lucide-react";
import {NativeSelect, NativeSelectOption} from "@/components/ui/native-select.tsx";

const historyBackground =
    "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer =
    "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const historyContainer =
    "p-0 pb-8";
const historyHeader =
    "flex flex-col items-center py-8 rounded-t-lg gap-3 px-5 " +
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]"
const historyTitle =
    "font-bold text-4xl text-center text-[var(--text-color)]";
const historySubtitle =
    "text-center text-[var(--text-light)] max-w-3xl";
const historyContent =
    "flex flex-col px-8 gap-4";
const historyAppointmentsCard =
    "px-6";
const historyUpperContainer =
    "flex flex-col sm:flex-row justify-between items-center";
const pastAppointments =
    "text-lg mb-2 sm:mb-0 text-[var(--text-color)] font-bold";
const historySort =
    "flex flex-row items-center gap-1 text-[var(--text-light)]";
const historySortIcon =
    "w-5 h-5 p-0 m-0";
const sortText =
    "text-md mr-2";

function MedicalHistory() {
    const patientId = "23";

    return (
      <div className={historyBackground}>
          <div className={cardContainer}>
              <Card className={historyContainer}>
                  <div className={historyHeader}>
                      <h1 className={historyTitle}>Medical History</h1>
                      <p className={historySubtitle}>View all your patients' medical files and appointment records</p>
                  </div>
                  <div className={historyContent}>
                      <PatientProfileCard patientId={patientId ?? ""} />
                      <Card className={historyAppointmentsCard}>
                          <div className={historyUpperContainer}>
                              <p className={pastAppointments}>Past Appointments</p>
                              <div className={historySort}>
                                  <ChevronsUpDown className={historySortIcon}></ChevronsUpDown>
                                  <p className={sortText}>Sort By</p>
                                  <NativeSelect>
                                      <NativeSelectOption value="oldest">Oldest First</NativeSelectOption>
                                      <NativeSelectOption value="newest">Newest First</NativeSelectOption>
                                  </NativeSelect>
                              </div>
                          </div>
                      </Card>
                      <NoPastAppointmentComponent />
                  </div>
              </Card>
          </div>
      </div>
    );
}

function PastAppointmentComponent() {
    return (
        <Card>

        </Card>
    );
}

const noAppointments=
    "flex flex-row items-center justify-center px-4 py-10 text-[var(--gray-500)] " +
    " bg-[var(--gray-100)] rounded-lg gap-2 border border-dashed border-[var(--gray-400)]";

function NoPastAppointmentComponent() {
    return (
        <div className={noAppointments}>
            <Info />
            <p>No past appointments.</p>
        </div>
    )
}

export default MedicalHistory;