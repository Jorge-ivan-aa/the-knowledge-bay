import {Steps} from "@ark-ui/react";

const Signup = ({data, state}) => {

    const fulldata = {
        username: data.username,
        email: data.email,
        password: data.password,
        dateofbirth: '',
        gender: '',
        phone: '',
        address: '',
    }

    console.log(fulldata);

    const items = [
        { value: 'first', title: 'First', description: 'Contact Info' },
        { value: 'second', title: 'Second', description: 'Date & Time' },
        { value: 'third', title: 'Third', description: 'Select Rooms' },
    ]
        return (
            <section className={ state ? "opacity-0 " : "opacity-100"}>
            <Steps.Root count={items.length}>
                <Steps.List>
                    {items.map((item, index) => (
                        <Steps.Item key={index} index={index}>
                            <Steps.Trigger>
                                <Steps.Indicator>{index + 1}</Steps.Indicator>
                                <span>{item.title}</span>
                            </Steps.Trigger>
                            <Steps.Separator />
                        </Steps.Item>
                    ))}
                </Steps.List>

                {items.map((item, index) => (
                    <Steps.Content key={index} index={index}>
                        {item.title} - {item.description}
                    </Steps.Content>
                ))}

                <Steps.CompletedContent>Steps Complete - Thank you for filling out the form!</Steps.CompletedContent>

                <div>
                    <Steps.PrevTrigger>Back</Steps.PrevTrigger>
                    <Steps.NextTrigger>Next</Steps.NextTrigger>
                </div>
            </Steps.Root>
                </section>
        )
}

export default Signup;