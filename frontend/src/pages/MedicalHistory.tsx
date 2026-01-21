import { Card } from "@/components/ui/card.tsx";
import PatientProfileCard from "@/components/PatientProfileCard.tsx";
// import {ChevronsUpDown, Info} from "lucide-react";
import {ChevronsUpDown} from "lucide-react";
import {NativeSelect, NativeSelectOption} from "@/components/ui/native-select.tsx";
import {useTranslation} from "react-i18next";
import PastAppointmentComponent from "@/components/PastAppointmentComponent.tsx";

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
const pastAppointmentsContainer =
    "flex flex-col gap-4"

function MedicalHistory() {
    const { t } = useTranslation();
    const patientId = "23";

    return (
      <div className={historyBackground}>
          <div className={cardContainer}>
              <Card className={historyContainer}>
                  <div className={historyHeader}>
                      <h1 className={historyTitle}>{t("medical-history.title")}</h1>
                      <p className={historySubtitle}>{t("medical-history.subtitle")}</p>
                  </div>
                  <div className={historyContent}>
                      <PatientProfileCard patientId={patientId ?? ""} />
                      <Card className={historyAppointmentsCard}>
                          <div className={historyUpperContainer}>
                              <p className={pastAppointments}>{t("medical-history.past")}</p>
                              <div className={historySort}>
                                  <ChevronsUpDown className={historySortIcon}></ChevronsUpDown>
                                  <p className={sortText}>{t("medical-history.sort.title")}</p>
                                  <NativeSelect className="cursor-pointer">
                                      <NativeSelectOption value="oldest">{t("medical-history.sort.old-first")}</NativeSelectOption>
                                      <NativeSelectOption value="newest">{t("medical-history.sort.new-first")}</NativeSelectOption>
                                  </NativeSelect>
                              </div>
                          </div>
                      </Card>
                      <div className={pastAppointmentsContainer}>
                          <PastAppointmentComponent />
                          <PastAppointmentComponent />
                          <PastAppointmentComponent />
                          <PastAppointmentComponent />
                      </div>
                  </div>
              </Card>
          </div>
      </div>
    );
}

// const noAppointments=
//     "flex flex-row items-center justify-center px-4 py-10 text-[var(--gray-500)] " +
//     " bg-[var(--gray-100)] rounded-lg gap-2 border border-dashed border-[var(--gray-400)]";
//
// function NoPastAppointmentComponent() {
//     const { t } = useTranslation();
//     return (
//         <div className={noAppointments}>
//             <Info />
//             <p>{t("medical-history.no-appointments")}</p>
//         </div>
//     )
// }

export default MedicalHistory;