import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Button} from "@/components/ui/button.tsx";
import {CalendarClock, Plus, X} from "lucide-react";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import {useTranslation} from "react-i18next";
import {useState} from "react";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import {Card} from "@/components/ui/card.tsx";
import {
    Dialog, DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle
} from "@/components/ui/dialog.tsx";
import {DatePicker} from "@/components/ui/date-picker.tsx";

const editButton =
    "mt-2 sm:mt-0 bg-transparent text-(--primary-color) hover:bg-(--primary-bg) cursor-pointer";
const unavailabilityContainer =
    "flex flex-col gap-6";
const unavailabilityEmptyContentContainer =
    "flex flex-col items-center gap-6";
const unavailabilityContentContainer =
    "flex flex-wrap gap-2 justify-start";
const dialogHeader =
    "font-bold text-xl text-[var(--text-color)]";
const dialogText =
    "text-[var(--text-light)] text-base font-normal leading-snug mt-1";
const dialogContent =
    "flex flex-col gap-4";
const dateFieldsGrid =
    "grid grid-cols-1 sm:grid-cols-2 gap-4";
const dateField =
    "flex flex-col gap-2 sm:mb-2 sm:mt-1";
const dateFieldLabel =
    "text-sm font-[500] text-(--text-color)";
const datePickerError =
    "border-[var(--danger)] ring-1 ring-[var(--danger)] bg-(--danger-lighter) hover:bg-(--danger-lighter)";
const dialogErrorText =
    "text-sm text-[var(--danger)] sm:-mt-2";
const dialogFooter =
    "mt-2";
const dialogCancel =
    "bg-white text-(--primary-color) border border-(--primary-color) " +
    "hover:text-white hover:bg-(--primary-dark) hover:border-(--primary-dark) cursor-pointer";
const dialogConfirm =
    "bg-(--primary-color) border border-(--primary-color) " +
    "hover:text-white hover:bg-(--primary-dark) hover:border-(--primary-dark) cursor-pointer";

export default function Unavailability() {
    const { t } = useTranslation();

    const [addOpen, setAddOpen] = useState(false);

    const [startDate, setStartDate] = useState<Date | undefined>(undefined);
    const [endDate, setEndDate] = useState<Date | undefined>(undefined);

    const onConfirmAdd = () => {
        setAddOpen(false);
    };

    const isLoading = false;
    const isUnavailability = true;

    const isRangeInvalid = !!startDate && !!endDate && endDate < startDate;

    return (
      <DashboardNavContainer>
          <DashboardNavHeader title={"Unavailability"}>
              <Button className={editButton} onClick={() => setAddOpen(true)}>
                  <Plus className="h-5 w-5" />
                  Add New
              </Button>
          </DashboardNavHeader>

          <Dialog
              open={addOpen}
              onOpenChange={(next) => {
                  setAddOpen(next);
                  if (!next) {
                      setStartDate(undefined);
                      setEndDate(undefined);
                  }
              }}
          >
              <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
                  <DialogHeader className={dialogHeader}>
                      <DialogTitle>
                          Add Unavailability
                      </DialogTitle>
                      <DialogDescription className={dialogText}>
                          Select the date range for your unavailability.
                      </DialogDescription>
                  </DialogHeader>
                  <div className={dialogContent}>
                      <div className={dateFieldsGrid}>
                          <div className={dateField}>
                              <p className={dateFieldLabel}>Start Date</p>
                              <DatePicker
                                  value={startDate}
                                  onChange={(d) => setStartDate(d)}
                                  className={isRangeInvalid ? datePickerError : undefined}
                              />
                          </div>

                          <div className={dateField}>
                              <p className={dateFieldLabel}>End Date</p>
                              <DatePicker
                                  value={endDate}
                                  onChange={(d) => setEndDate(d)}
                                  className={isRangeInvalid ? datePickerError : undefined}
                              />
                          </div>
                      </div>

                      {isRangeInvalid && (
                          <p className={dialogErrorText}>
                              End date can’t be earlier than start date.
                          </p>
                      )}
                  </div>

                  <DialogFooter className={dialogFooter}>
                      <DialogClose asChild>
                          <Button type="button" className={dialogCancel}>
                              Cancel
                          </Button>
                      </DialogClose>

                      <Button
                          type="button"
                          className={dialogConfirm}
                          onClick={onConfirmAdd}
                          disabled={!startDate || !endDate || isRangeInvalid}
                      >
                          Add Unavailability
                      </Button>
                  </DialogFooter>
              </DialogContent>
          </Dialog>

          {isLoading ? (
              <DashboardNavLoader />
          ) : (
              <div className={unavailabilityContainer}>
                  {!isUnavailability ? (
                      <div className={unavailabilityEmptyContentContainer}>
                          <DashboardNavEmptyContent
                              title={"No unavailability set up"}
                              text={"Your unavailability will appear here once you have added one."}
                              Icon={CalendarClock}
                          />
                      </div>
                  ) : (
                      <div className={unavailabilityContentContainer}>
                          <UnavailabilityItem />
                          <UnavailabilityItem />
                          <UnavailabilityItem />
                          <UnavailabilityItem />
                          <UnavailabilityItem />
                          <UnavailabilityItem />
                      </div>
                  )}
                  <div />
              </div>
          )}
      </DashboardNavContainer>
    );
}

const unavailabilityItemCard =
    "w-[200px] p-0 flex flex-row gap-0 items-start relative";
const unavailabilityItemContent =
    "flex flex-col py-2 px-3 pr-10";
const unavailabilityItemTitle =
    "text-sm font-medium";
const unavailabilityItemDate =
    "text-sm text-(--text-light)";
const unavailabilityItemDeleteButton =
    "cursor-pointer hover:bg-transparent hover:text-(--danger) transition-none h-6 w-6 p-0 absolute top-2 right-2";
const dialogDelete =
    "text-white bg-[var(--danger)] border border-[var(--danger)] hover:text-white hover:bg-[var(--danger-dark)] hover:border hover:border-[var(--danger-dark)] cursor-pointer";

function UnavailabilityItem() {
    const isSingleDate = true;

    const [deleteOpen, setDeleteOpen] = useState(false);

    const onConfirmDelete = () => {
        setDeleteOpen(false);
    };

    return (
        <Card className={unavailabilityItemCard}>
            <div className={unavailabilityItemContent}>
                <h3 className={unavailabilityItemTitle}>Single Day</h3>
                <p className={unavailabilityItemDate}>January 30th, 2026</p>
            </div>
            <Button type="button" variant="ghost" className={unavailabilityItemDeleteButton} onClick={() => setDeleteOpen(true)}>
                <X className="w-4 h-4" />
            </Button>

            <Dialog
                open={deleteOpen}
                onOpenChange={setDeleteOpen}
            >
                <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
                    <DialogHeader className={dialogHeader}>
                        <DialogTitle>
                            Delete Unavailability
                        </DialogTitle>

                        <DialogDescription className={dialogText}>
                            Are you sure you want to delete this date? This action can’t be undone.
                        </DialogDescription>
                    </DialogHeader>

                    <DialogFooter className={dialogFooter}>
                        <DialogClose asChild>
                            <Button type="button" className={dialogCancel}>
                                Cancel
                            </Button>
                        </DialogClose>

                        <Button type="button" className={dialogDelete} onClick={onConfirmDelete}>
                            Delete
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </Card>
    );
}