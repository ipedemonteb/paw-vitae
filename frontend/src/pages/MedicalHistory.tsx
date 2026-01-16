import { Card } from "@/components/ui/card.tsx";
import PatientProfileCard from "@/components/PatientProfileCard.tsx";
import {ChevronsUpDown, Paperclip, ChevronDown} from "lucide-react";
import {NativeSelect, NativeSelectOption} from "@/components/ui/native-select.tsx";
import {useTranslation} from "react-i18next";
import {Badge} from "@/components/ui/badge.tsx";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {Button} from "@/components/ui/button.tsx";


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
                                  <NativeSelect className="cursor-pointer">
                                      <NativeSelectOption value="oldest">Oldest First</NativeSelectOption>
                                      <NativeSelectOption value="newest">Newest First</NativeSelectOption>
                                  </NativeSelect>
                              </div>
                          </div>
                      </Card>
                      <PastAppointmentComponent />
                  </div>
              </Card>
          </div>
      </div>
    );
}

const appointmentContainer =
    "w-full border border-solid rounded-md overflow-hidden flex flex-col shadow sm:flex-row py-0 gap-0";
const leftSection =
    "bg-gray-100 w-full max-w-none min-w-0 space-y-3 flex flex-col justify-center items-center py-6 sm:py-8 sm:w-1/6 sm:max-w-44 sm:min-w-36 self-stretch rounded-t-md sm:rounded-t-none sm:rounded-l-md";
const dateBlock =
    "text-sm flex flex-col gap-0 items-center justify-center text-center w-full";
const monthText =
    "text-(--text-light)";
const dayText =
    "text-3xl font-bold";
const yearText =
    "text-(--text-light)";
const statusRow =
    "w-full flex justify-center items-center opacity-80";
const badge =
    "bg-transparent text-[var(--primary-color)] border border-[var(--primary-color)] px-2 py-1 rounded-full font-medium";
const middleSection =
    "min-w-0 px-5 py-4 w-full flex flex-col items-center sm:items-start sm:h-full sm:w-5/6 sm:py-6";
const avatarContainer =
    "flex items-center w-12 h-12 border border-[var(--primary-light)] border-2 rounded-full sm:mb-0";
const doctorAvatar =
    "flex flex-row items-center gap-2";
const nameBlock =
    "flex flex-col min-w-0";
const fullNameText =
    "font-semibold";
const coverageRow =
    "text-(--text-light) text-xs flex flex-row items-center gap-2 min-w-0";
const coverageDot =
    "bg-[var(--primary-color)] w-2 h-2 rounded-full shrink-0";
const middleRow =
    "flex w-full py-4";
const reasonWrapper =
    "h-full w-full ml-0";
const reasonBar =
    "w-3 bg-blue-600 h-full rounded-sm shrink-0";
const reasonBox =
    "min-w-0 rounded-sm h-full w-full text-xs bg-gray-50 flex flex-col px-3 justify-center py-3";
const reasonLabel =
    "text-(--text-light)";
const reasonTextWrap =
    "relative w-full min-w-0";
const reasonText =
    "truncate block w-full";
const reasonFade =
    "pointer-events-none absolute top-0 right-0 h-full w-8 bg-linear-to-r from-transparent to-gray-50";
const bottomRow =
    "flex flex-col gap-3 items-stretch w-full sm:flex-row sm:justify-between sm:items-center";
const specialtyPill =
    "flex gap-1 rounded-2xl font-[500] text-xs text-[var(--gray-600)] h-min py-1.5 px-2.5 text-sm flex items-center justify-center border border-[var(--gray-500)]";
const detailsButton =
    "flex gap-2 text-sm font-[400] hover:bg-(--primary-dark) cursor-pointer items-center text-white justify-center px-2 py-2 bg-(--primary-color)";
const filesIcon =
    "w-4 h-4";

function PastAppointmentComponent() {
    const { i18n } = useTranslation();
    const locale = i18n?.language || "en-US";
    const d = new Date("2025-01-01T12:00:00");
    const day = d.getDate();
    const month = new Intl.DateTimeFormat(locale, { month: "long" }).format(d).toUpperCase();
    const year = d.getFullYear()

    return (
        <Card className={appointmentContainer}>
            <div className={leftSection}>
                <div className={dateBlock}>
                    <span className={monthText}>{month}</span>
                    <span className={dayText}>{day}</span>
                    <span className={yearText}>{year}</span>
                </div>
                <div className={statusRow}>
                    <Badge className={badge}>
                        Endocrinology
                    </Badge>
                </div>
            </div>
            <div className={middleSection}>
                <div className={doctorAvatar}>
                    <Avatar className={avatarContainer}>
                        <AvatarImage src="https://avatars.dicebear.com/api/jdenticon/JD.svg" />
                        <AvatarFallback>JD</AvatarFallback>
                    </Avatar>
                    <div className={nameBlock}>
                        <span className={fullNameText}>
                            {"John Doe"}
                        </span>
                        <span className={coverageRow}>
                            <span className={coverageDot} />
                            {"OSDE"}
                        </span>
                    </div>
                </div>
                <div className={middleRow}>
                    <div className={reasonWrapper}>
                        <div className={reasonBar} />
                        <div className={reasonBox}>
                            {"adasdasdasdasd".trim().length > 0 ? (
                                <>
                                    <span className={reasonLabel}>
                                Reason for Visit:
                            </span>
                                    <div className={reasonTextWrap}>
                                <span className={reasonText}>
                                    "adasdasdasdasd"
                                </span>
                                        <div className={reasonFade} />
                                    </div>
                                </>
                            ) : (
                                <span className="text-(--text-light)">No Reason Specified</span>
                            )}
                        </div>
                    </div>
                </div>
                <div className={bottomRow}>
                    <div className={specialtyPill}>
                        <Paperclip className={filesIcon}/>
                        2 Files
                    </div>
                    <Button className={detailsButton}>
                        <ChevronDown />
                        View Details
                    </Button>
                </div>
            </div>
        </Card>
    );
}

// const noAppointments=
//     "flex flex-row items-center justify-center px-4 py-10 text-[var(--gray-500)] " +
//     " bg-[var(--gray-100)] rounded-lg gap-2 border border-dashed border-[var(--gray-400)]";
//
// function NoPastAppointmentComponent() {
//     return (
//         <div className={noAppointments}>
//             <Info />
//             <p>No past appointments.</p>
//         </div>
//     )
// }

export default MedicalHistory;